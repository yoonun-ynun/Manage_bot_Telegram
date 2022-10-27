import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class gethitomi {
    void getimage(String key) throws Exception{
        String URL = "https://hitomi.la/reader/" + key + ".html#1";

        Path path = Paths.get("/Users/yoonun/chromedriver");
        System.setProperty("webdriver.chrome.driver", path.toString());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.setHeadless(true);
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-default-apps");

        WebDriver driver = new ChromeDriver();

        driver.get(URL);

        Thread.sleep(1000);

        String image = driver.findElement(By.xpath("/html/body/div[3]/picture/img")).getAttribute("src");
        driver.close();


        java.net.URL url = new URL(image);
        HttpURLConnection in = (HttpURLConnection) url.openConnection();
        in.setRequestProperty("Referer", URL);
        in.setRequestMethod("GET");

        InputStream is = in.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(new File("./web/WEB-INF","image.webp"));

        final int BUFFER_SIZE = 4096;
        int bytesRead;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = is.read(buffer)) != -1){
            outputStream.write(buffer, 0, bytesRead);
        }
    }
}
