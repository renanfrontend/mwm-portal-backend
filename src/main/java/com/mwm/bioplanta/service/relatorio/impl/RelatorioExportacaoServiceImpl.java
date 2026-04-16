package com.mwm.bioplanta.service.relatorio.impl;

import com.mwm.bioplanta.dto.relatorio.ExportarRelatorioRequest;
import com.mwm.bioplanta.model.BioAgendaPlanejada;
import com.mwm.bioplanta.repository.agenda.BioAgendaPlanejadaRepository;
import com.mwm.bioplanta.repository.cooperado.BioEstabelecimentoRepository;
import com.mwm.bioplanta.model.BioEstabelecimento;
import com.mwm.bioplanta.service.relatorio.RelatorioExportacaoService;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioExportacaoServiceImpl implements RelatorioExportacaoService {
    
    private final BioAgendaPlanejadaRepository bioAgendaPlanejadaRepository;
    private final BioEstabelecimentoRepository bioEstabelecimentoRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(RelatorioExportacaoServiceImpl.class);
    private static final String[] DIAS_SEMANA = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"};
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM");
    
    public RelatorioExportacaoServiceImpl(BioAgendaPlanejadaRepository bioAgendaPlanejadaRepository, BioEstabelecimentoRepository bioEstabelecimentoRepository) {
        this.bioAgendaPlanejadaRepository = bioAgendaPlanejadaRepository;
        this.bioEstabelecimentoRepository = bioEstabelecimentoRepository;
    }
    
    @Override
    public ByteArrayResource exportar(ExportarRelatorioRequest request, MediaType mediaType) {
        if (MediaType.APPLICATION_PDF.equals(mediaType)) {
            return exportarPdf(request);
        } else {
            return exportarCsv(request);
        }
    }

    private ByteArrayResource exportarCsv(ExportarRelatorioRequest request) {
        try {
            logger.info("🔄 Iniciando exportação CSV");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataInicial = LocalDate.parse(request.getDataInicial(), formatter);
            LocalDate dataFinal = LocalDate.parse(request.getDataFinal(), formatter);
            String transportadora = request.getTransportadora();
            
            logger.info("📅 Filtros: Período {} a {}, Transportadora: {}", 
                dataInicial, dataFinal, transportadora != null ? transportadora : "TODAS");
            
            List<LocalDate> datas = gerarDatasDoPerido(dataInicial, dataFinal);
            logger.info("📅 {} dias no período", datas.size());
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            baos.write(0xEF);
            baos.write(0xBB);
            baos.write(0xBF);
            
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(baos, StandardCharsets.UTF_8));
            
            writer.write(gerarCabecalho(datas));
            
            List<BioAgendaPlanejada> registros = bioAgendaPlanejadaRepository
                    .findByIdBioplantaAndIdFiliadaAndDataAgendadaBetween(1L, 1L, dataInicial, dataFinal);
            
            if (transportadora != null && !transportadora.isEmpty()) {
                registros.removeIf(reg -> reg.getTransportadora() == null || 
                                         !reg.getTransportadora().equals(transportadora));
            }
            
            if (registros.isEmpty()) {
                logger.warn("⚠️ Nenhum registro encontrado para o período");
                writer.flush();
                return new ByteArrayResource(baos.toByteArray());
            }
            
            logger.info("📊 {} registros encontrados no banco", registros.size());
            
            Map<Long, List<BioAgendaPlanejada>> porEstabelecimento = new LinkedHashMap<>();
            for (BioAgendaPlanejada reg : registros) {
                porEstabelecimento.computeIfAbsent(reg.getIdEstabelecimento(), k -> new ArrayList<>())
                    .add(reg);
            }
            
            int linhasProcessadas = 0;
            for (List<BioAgendaPlanejada> registrosEstab : porEstabelecimento.values()) {
                try {
                    BioAgendaPlanejada primeiro = registrosEstab.get(0);
                    
                    // Busca o número do estabelecimento pelo id
                    String numEstab = "";
                    try {
                        BioEstabelecimento est = bioEstabelecimentoRepository.findById(primeiro.getIdEstabelecimento()).orElse(null);
                        numEstab = (est != null && est.getNumeroEstabelecimento() != null) ? est.getNumeroEstabelecimento() : String.valueOf(primeiro.getIdEstabelecimento());
                    } catch (Exception ex) {
                        numEstab = String.valueOf(primeiro.getIdEstabelecimento());
                    }
                    String produtor = escapeCsv(primeiro.getProdutor());
                    String distancia = formatarDistancia(primeiro.getDistanciaKm());
                    String transportadoraEstab = obterTransportadora(registrosEstab);
                    
                    Map<LocalDate, Integer> coletasPorDia = new HashMap<>();
                    for (BioAgendaPlanejada reg : registrosEstab) {
                        coletasPorDia.put(reg.getDataAgendada(), reg.getQtdViagens() != null ? reg.getQtdViagens() : 0);
                    }
                    
                    int totalViagens = coletasPorDia.values().stream().mapToInt(Integer::intValue).sum();
                    int totalKm = (primeiro.getDistanciaKm() != null ? primeiro.getDistanciaKm() : 0) * totalViagens;
                    
                    StringBuilder linha = new StringBuilder();
                    linha.append(numEstab).append(";");
                    linha.append(produtor).append(";");
                    linha.append(distancia).append(";");
                    linha.append(transportadoraEstab).append(";");
                    linha.append(formatarTotalKm(totalKm));
                    
                    for (LocalDate data : datas) {
                        int coletas = coletasPorDia.getOrDefault(data, 0);
                        linha.append(";").append(coletas);
                    }
                    
                    linha.append("\n");
                    writer.write(linha.toString());
                    linhasProcessadas++;
                    
                } catch (Exception e) {
                    logger.error("❌ Erro ao processar linha: {}", e.getMessage());
                    throw e;
                }
            }
            
            writer.flush();
            byte[] csvBytes = baos.toByteArray();
            logger.info("✅ CSV gerado com sucesso. {} linhas exportadas. Tamanho: {} bytes", 
                linhasProcessadas, csvBytes.length);
            
            return new ByteArrayResource(csvBytes);
            
        } catch (Exception e) {
            logger.error("❌ Erro ao gerar CSV: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar CSV: " + e.getMessage(), e);
        }
    }

    private ByteArrayResource exportarPdf(ExportarRelatorioRequest request) {
        try {
            logger.info("🔄 Iniciando exportação PDF");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataInicial = LocalDate.parse(request.getDataInicial(), formatter);
            LocalDate dataFinal = LocalDate.parse(request.getDataFinal(), formatter);
            String transportadora = request.getTransportadora();
            
            logger.info("📅 Filtros: Período {} a {}, Transportadora: {}", 
                dataInicial, dataFinal, transportadora != null ? transportadora : "TODAS");
            
            List<LocalDate> datas = gerarDatasDoPerido(dataInicial, dataFinal);
            logger.info("📅 {} dias no período", datas.size());
            
            // Busca registros
            List<BioAgendaPlanejada> registros = bioAgendaPlanejadaRepository
                    .findByIdBioplantaAndIdFiliadaAndDataAgendadaBetween(1L, 1L, dataInicial, dataFinal);
            
            if (transportadora != null && !transportadora.isEmpty()) {
                registros.removeIf(reg -> reg.getTransportadora() == null || 
                                         !reg.getTransportadora().equals(transportadora));
            }
            
            if (registros.isEmpty()) {
                logger.warn("⚠️ Nenhum registro encontrado para o período");
                return new ByteArrayResource(new byte[0]);
            }
            
            logger.info("📊 {} registros encontrados no banco", registros.size());
            
            // Agrupa por estabelecimento
            Map<Long, List<BioAgendaPlanejada>> porEstabelecimento = new LinkedHashMap<>();
            for (BioAgendaPlanejada reg : registros) {
                porEstabelecimento.computeIfAbsent(reg.getIdEstabelecimento(), k -> new ArrayList<>())
                    .add(reg);
            }
            
            // Cria documento PDF com iText
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // Adiciona logo à esquerda e marca-d'água à direita no topo
            try {
                java.io.InputStream logoStream = getClass().getClassLoader().getResourceAsStream("logo-mwm.png");
                java.io.InputStream marcaDaguaStream = getClass().getClassLoader().getResourceAsStream("db/marca-dagua-direita.png");
                if (logoStream != null && marcaDaguaStream != null) {
                    byte[] logoBytes = logoStream.readAllBytes();
                    byte[] marcaDaguaBytes = marcaDaguaStream.readAllBytes();
                    Image logo = new Image(ImageDataFactory.create(logoBytes));
                    Image marcaDagua = new Image(ImageDataFactory.create(marcaDaguaBytes));
                    float alturaLogo = 70f;
                    float alturaMarca = 70f;
                    logo.scaleToFit(alturaLogo, alturaLogo);
                    marcaDagua.scaleToFit(alturaMarca, alturaMarca);
                    marcaDagua.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                    marcaDagua.setMarginRight(0);
                    marcaDagua.setPaddingRight(0);
                    Table headerTable = new Table(new float[]{1, 1});
                    headerTable.setWidth(UnitValue.createPercentValue(100));
                    headerTable.addCell(new Cell().add(logo).setBorder(null).setPadding(0).setTextAlignment(com.itextpdf.layout.property.TextAlignment.LEFT));
                    Cell cellMarca = new Cell().add(marcaDagua).setBorder(null).setPadding(0).setTextAlignment(com.itextpdf.layout.property.TextAlignment.RIGHT);
                    cellMarca.setMarginRight(0);
                    cellMarca.setPaddingRight(0);
                    headerTable.addCell(cellMarca);
                    document.add(headerTable);
                }
            } catch (Exception e) {
                logger.warn("Não foi possível carregar logo ou marca d'água: {}", e.getMessage());
            }

            // Título
            document.add(new Paragraph("RELATÓRIO DE PLANEJAMENTO")
                .setFontSize(16)
                .setBold());
            
            // Informações
            String periodoFormatado = "Período: " + dataInicial.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " a " + dataFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            document.add(new Paragraph(periodoFormatado)
                .setFontSize(10));
            document.add(new Paragraph("Transportadora: " + (transportadora != null && !transportadora.isEmpty() ? transportadora : "TODAS"))
                .setFontSize(10));
            document.add(new Paragraph("\n"));
            
            // Cria tabela com número de colunas (Est + Produtor + Dist + Transp + Total KM + dias)
            // Cabeçalhos
            DeviceRgb cinzaSuave = new DeviceRgb(245, 245, 245); // Cinza mais claro
            DeviceRgb fonteSuave = new DeviceRgb(80, 80, 80); // Cinza escuro para fonte
            float[] colWidths = new float[]{2.2f, 4.5f, 1.7f, 1.5f, 2.0f};
            float[] finalColWidths = new float[colWidths.length + datas.size()];
            System.arraycopy(colWidths, 0, finalColWidths, 0, colWidths.length);
            for (int i = 0; i < datas.size(); i++) finalColWidths[colWidths.length + i] = 1.0f;
            Table table = new Table(finalColWidths);
            float fontSizeHeader = 8f;
            // Título ajustado para 'Estabelecimento'
            table.addHeaderCell(new Cell().add(new Paragraph("Estabelecimento").setBold().setFontColor(fonteSuave).setFontSize(fontSizeHeader)).setBackgroundColor(cinzaSuave));
            table.addHeaderCell(new Cell().add(new Paragraph("Produtor").setBold().setFontColor(fonteSuave).setFontSize(fontSizeHeader)).setBackgroundColor(cinzaSuave));
            table.addHeaderCell(new Cell().add(new Paragraph("Distância").setBold().setFontColor(fonteSuave).setFontSize(fontSizeHeader)).setBackgroundColor(cinzaSuave));
            table.addHeaderCell(new Cell().add(new Paragraph("Transportadora").setBold().setFontColor(fonteSuave).setFontSize(fontSizeHeader)).setBackgroundColor(cinzaSuave));
            table.addHeaderCell(new Cell().add(new Paragraph("Total KM").setBold().setFontColor(fonteSuave).setFontSize(fontSizeHeader)).setBackgroundColor(cinzaSuave));
            for (LocalDate data : datas) {
                int diaSemana = data.getDayOfWeek().getValue() % 7;
                String dia = DIAS_SEMANA[diaSemana];
                String dataFormatada = data.format(DATE_FORMATTER);
                table.addHeaderCell(new Cell().add(new Paragraph(dia + " " + dataFormatada).setBold().setFontSize(fontSizeHeader).setFontColor(fonteSuave)).setBackgroundColor(cinzaSuave));
            }
            
            // Dados das linhas
            int linhasProcessadas = 0;
            for (List<BioAgendaPlanejada> registrosEstab : porEstabelecimento.values()) {
                BioAgendaPlanejada primeiro = registrosEstab.get(0);
                
                // Busca o número do estabelecimento pelo id
                String numEstab = "";
                try {
                    BioEstabelecimento est = bioEstabelecimentoRepository.findById(primeiro.getIdEstabelecimento()).orElse(null);
                    numEstab = (est != null && est.getNumeroEstabelecimento() != null) ? est.getNumeroEstabelecimento() : String.valueOf(primeiro.getIdEstabelecimento());
                } catch (Exception ex) {
                    numEstab = String.valueOf(primeiro.getIdEstabelecimento());
                }
                String produtor = primeiro.getProdutor();
                String distancia = formatarDistancia(primeiro.getDistanciaKm());
                String transportadoraEstab = obterTransportadora(registrosEstab);
                
                Map<LocalDate, Integer> coletasPorDia = new HashMap<>();
                for (BioAgendaPlanejada reg : registrosEstab) {
                    coletasPorDia.put(reg.getDataAgendada(), reg.getQtdViagens() != null ? reg.getQtdViagens() : 0);
                }
                
                int totalViagens = coletasPorDia.values().stream().mapToInt(Integer::intValue).sum();
                int totalKm = (primeiro.getDistanciaKm() != null ? primeiro.getDistanciaKm() : 0) * totalViagens;
                
                // Linhas normais sem fundo cinza
                table.addCell(new Cell().add(new Paragraph(numEstab).setFontSize(fontSizeHeader)));
                table.addCell(new Cell().add(new Paragraph(produtor).setFontSize(fontSizeHeader)));
                table.addCell(new Cell().add(new Paragraph(distancia).setFontSize(fontSizeHeader)));
                table.addCell(new Cell().add(new Paragraph(transportadoraEstab).setFontSize(fontSizeHeader)));
                table.addCell(new Cell().add(new Paragraph(formatarTotalKm(totalKm)).setFontSize(fontSizeHeader)));
                for (LocalDate data : datas) {
                    int coletas = coletasPorDia.getOrDefault(data, 0);
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(coletas)).setFontSize(8)));
                }
                
                linhasProcessadas++;
            }
            
            document.add(table);
            document.close();
            
            byte[] pdfBytes = baos.toByteArray();
            logger.info("✅ PDF gerado com sucesso. {} linhas exportadas. Tamanho: {} bytes", 
                linhasProcessadas, pdfBytes.length);
            
            return new ByteArrayResource(pdfBytes);
            
        } catch (Exception e) {
            logger.error("❌ Erro ao gerar PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }

    private String gerarCabecalho(List<LocalDate> datas) {
        StringBuilder cabecalho = new StringBuilder();
        cabecalho.append("N. Estabelecimento;Produtor;Distancia (KM);Transportadora;Total de KM");
        
        for (LocalDate data : datas) {
            int diaSemana = data.getDayOfWeek().getValue() % 7;
            String dia = DIAS_SEMANA[diaSemana];
            String dataFormatada = data.format(DATE_FORMATTER);
            
            cabecalho.append(";").append(dia).append(" ").append(dataFormatada);
        }
        
        cabecalho.append("\n");
        return cabecalho.toString();
    }

    private List<LocalDate> gerarDatasDoPerido(LocalDate dataInicial, LocalDate dataFinal) {
        List<LocalDate> datas = new ArrayList<>();
        LocalDate data = dataInicial;
        while (!data.isAfter(dataFinal)) {
            datas.add(data);
            data = data.plusDays(1);
        }
        return datas;
    }

    private String formatarDistancia(Integer distancia) {
        if (distancia == null || distancia == 0) {
            return "0";
        }
        return String.valueOf(distancia);
    }

    private String formatarTotalKm(Integer totalKm) {
        if (totalKm == null || totalKm == 0) {
            return "0";
        }
        return String.valueOf(totalKm);
    }

    private String obterTransportadora(List<BioAgendaPlanejada> registros) {
        if (registros == null || registros.isEmpty()) {
            return "";
        }
        for (BioAgendaPlanejada registro : registros) {
            if (registro.getTransportadora() != null && !registro.getTransportadora().isBlank()) {
                return escapeCsv(registro.getTransportadora());
            }
        }
        return "";
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.trim().replace("\"", "\"\"");
    }

    @Override
    public String getFileName(ExportarRelatorioRequest request) {
        String base = "relatorio_planejado";
        if (request.getFormato().equalsIgnoreCase("pdf")) {
            return base + ".pdf";
        } else {
            return base + ".csv";
        }
    }
}
