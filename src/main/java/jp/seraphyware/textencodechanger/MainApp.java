package jp.seraphyware.textencodechanger;

import jp.seraphyware.textencodechanger.ui.ScreensConfiguration;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.
        AnnotationConfigApplicationContext;

/**
 * アプリケーションのエントリ.
 *
 * @author seraphy
 */
public final class MainApp extends Application {

    /**
     * ロガー.
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Springのコンテキスト.
     */
    private AnnotationConfigApplicationContext context;

    /**
     * JavaFXアプリケーションの開始時に呼び出される.
     *
     * @param stage
     * @throws Exception なんらかの失敗
     */
    @Override
    public void start(final Stage stage) throws Exception {
        log.info("★MainApp::start");

        // Springのコンテキストを作成する.
        // JavaFXのlaunchにより、JavaFXスレッド上でコンテキストを構築する.
        context = new AnnotationConfigApplicationContext();
        //context.register(MainAppConfiguration.class); // 明示的なBeanの定義による方法
        context.scan("jp.seraphyware"); // スキャンによる方法
        context.scan("jp.seraphyware.services"); // スキャンによる方法
        context.scan("jp.seraphyware.ui"); // スキャンによる方法
        context.refresh();

        // スクリーン
        ScreensConfiguration screens = context.getBean(
                ScreensConfiguration.class);
        screens.setPrimaryStage(stage);
        screens.mainWindow().show();
    }

    /**
     * JavaFXアプリケーションの停止時に呼び出される.
     *
     * @throws Exception なんらかの失敗
     */
    @Override
    public void stop() throws Exception {
        log.info("★MainApp::stop");
        // コンテキストを終了する.
        context.close();
        super.stop();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
