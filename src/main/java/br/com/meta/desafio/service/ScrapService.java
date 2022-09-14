package br.com.meta.desafio.service;

import br.com.meta.desafio.mapper.FileMapper;
import br.com.meta.desafio.model.FileAttribute;
import br.com.meta.desafio.model.Repository;
import br.com.meta.desafio.util.FileUtil;
import br.com.meta.desafio.viewModel.FileInfo;
import br.com.meta.desafio.viewModel.RepositoryInfo;
import br.com.meta.desafio.viewModel.RepositoryResume;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ScrapService {
    private final RepositoryService service;

    public ScrapService(RepositoryService service) {
        this.service = service;
    }

    public RepositoryInfo scrap(String url) throws Exception {
        final Repository cachedRepository = service.getCachedRepository(url);
        final RepositoryInfo repositoryInfo = new RepositoryInfo();
        final RepositoryResume repositoryResume = new RepositoryResume();

        final List<FileInfo> fileInfos = new ArrayList<>();
        List<FileAttribute> fileAttributes = new ArrayList<>();

        String repoLastCommit = scrapPageGetLastCommit(url);

        if (cachedRepository != null && cachedRepository.getLastCommit().equals(repoLastCommit)) {
            fileAttributes.addAll(cachedRepository.getListFiles().stream()
                    .map(FileMapper::toAttribute)
                    .collect(Collectors.toList()));
        } else {
            fileAttributes.addAll(scrapPage(url));
        }

        service.cacheRepository(url, repoLastCommit, fileAttributes);

        fileAttributes.stream().collect(Collectors.groupingBy(FileAttribute::getExtension)).forEach((s, fileAttributes1) -> {
            int lines = fileAttributes1.stream().mapToInt(FileAttribute::getLines).sum();
            long totalSize = fileAttributes1.stream().mapToLong(FileAttribute::getSize).sum();

            BigDecimal percentInRepo = BigDecimal.valueOf(((double) fileAttributes1.size() / (double) fileAttributes.size()) * 100);
            percentInRepo = percentInRepo.setScale(2, RoundingMode.HALF_UP);

            fileInfos.add(new FileInfo(s, fileAttributes1.size(), lines, percentInRepo.doubleValue(), FileUtil.convertFileSizeToString(totalSize)));
        });

        repositoryResume.setUrl(url);
        repositoryResume.setFilesNumber(fileAttributes.size());
        repositoryResume.setSize(FileUtil.convertFileSizeToString(fileAttributes.stream().mapToLong(FileAttribute::getSize).sum()));

        repositoryInfo.setResume(repositoryResume);
        repositoryInfo.setFileInfos(fileInfos);

        return repositoryInfo;

    }

    private String scrapPageGetLastCommit(String url) throws Exception {
        Document webPage = Jsoup.connect(url).get();

        Elements commitElement = webPage.getElementsByClass("d-none js-permalink-shortcut");

        return commitElement.get(0).attr("href");
    }

    private List<FileAttribute> scrapPage(String url) throws Exception {
        List<FileAttribute> files = new ArrayList<>();

        Document webPage = Jsoup.connect(url).get();

        Elements fileGridItens = webPage.getElementsByClass("Box-row Box-row--focus-gray py-2 d-flex position-relative js-navigation-item ");

        fileGridItens.forEach(e -> {
            Element infoElement = e.getElementsByClass("mr-3 flex-shrink-0").first();
            Element nameElement = e.getElementsByClass("flex-auto min-width-0 col-md-2 mr-3").first();

            final String type = infoElement.childNodes().get(1).attr("aria-label");
            final String name = (nameElement.childNodes().get(1).childNodes().get(0)).attr("title");
            final String itemUrl = (nameElement.childNodes().get(1).childNodes().get(0)).attr("href");

            if (type.equals("Directory")) {
                try {
                    files.addAll(scrapPage("https://www.github.com".concat(itemUrl)));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if (type.equals("File")) {
                try {
                    files.add(scrapFilePage(name, "https://www.github.com".concat(itemUrl)));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return files;
    }

    private FileAttribute scrapFilePage(String fileName, String url) throws Exception {
        final FileAttribute fileAttribute = new FileAttribute();

        Document webPage = Jsoup.connect(url).get();

        Elements fileGridItens = webPage.getElementsByClass("text-mono f6 flex-auto pr-3 flex-order-2 flex-md-order-1");

        fileGridItens.forEach(e -> {
            String linesInfo = e.text();
            int fileLines = FileUtil.extractFileLines(linesInfo);
            long fileSize = FileUtil.extractFileSizeInBytes(linesInfo).longValue();
            int indexOfExtension = fileName.lastIndexOf(".");

            if (indexOfExtension == -1) {
                fileAttribute.setExtension(fileName);
            } else {
                fileAttribute.setExtension(fileName.substring(fileName.lastIndexOf(".")));
            }

            fileAttribute.setLines(fileLines);
            fileAttribute.setSize(fileSize);
        });

        return fileAttribute;
    }
}