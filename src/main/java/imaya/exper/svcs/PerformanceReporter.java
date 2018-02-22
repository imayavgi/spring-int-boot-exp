package imaya.exper.svcs;

import com.jamonapi.MonitorFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

//@Service
public class PerformanceReporter {

    @PostConstruct
    public void initFile() {
        try {
            FileUtils.writeStringToFile(FileUtils.getFile("/tmp/report.html"), "<html>", (Charset)null, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay = 60000, fixedDelay=60000)
    public void reportStat() {
        String report = MonitorFactory.getReport();
        try {
            FileUtils.writeStringToFile(FileUtils.getFile("/tmp/report.html"), report, (Charset)null, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void closeFile() {
        try {
            FileUtils.writeStringToFile(FileUtils.getFile("/tmp/report.html"), "</html>", (Charset)null, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
