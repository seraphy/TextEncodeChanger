package jp.seraphyware.textencodechanger;

import java.awt.SplashScreen;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import javax.swing.SwingUtilities;
import jp.seraphyware.textencodechanger.ui.MainWndController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.
        AnnotationConfigApplicationContext;

/**
 * アプリケーションのエントリ.
 *
 * @author seraphy
 */
public class MainApp extends Application {

    /**
     * ロガー.
     */
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    /**
     * Springのコンテキスト.
     */
    private AnnotationConfigApplicationContext context;
    
    /**
     * メインウィンドウ
     */
    @Autowired
    private MainWndController mainWindow;

    /**
     * JavaFXアプリケーションの初期化時に呼び出される.
     * @throws Exception 
     */
    @Override
    public void init() throws Exception {
        log.info("★MainApp::init");

        // Springのコンテキストを作成する.
        // JavaFXのlaunchにより、JavaFXスレッド上でコンテキストを構築する.
        context = new AnnotationConfigApplicationContext();
        //context.register(MainAppConfiguration.class); // 明示的なBeanの定義による方法
        context.scan("jp.seraphyware"); // スキャンによる方法
        context.scan("jp.seraphyware.services"); // スキャンによる方法
        context.scan("jp.seraphyware.ui"); // スキャンによる方法
        context.refresh();
        
        // このインスタンスを管理ビーンにする
        // 参考: https://ksby.hatenablog.com/entry/2016/08/14/185055
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
        beanFactory.initializeBean(this, getClass().getSimpleName());
        ((ConfigurableListableBeanFactory) context.getBeanFactory())
                .registerSingleton(getClass().getSimpleName(), this);
    }

    /**
     * JavaFXアプリケーションの開始時に呼び出される.
     *
     * @param stage
     * @throws Exception なんらかの失敗
     */
    @Override
    public void start(final Stage stage) throws Exception {
        log.info("★MainApp::start");

        // メインウィンドウの表示
        Stage stg = mainWindow.getStage();
        stg.show();
        
        // スプラッシュが表示されていれば非表示にする
        SwingUtilities.invokeLater(() -> {
            try {
                SplashScreen splashScreen = SplashScreen.getSplashScreen();
                if (splashScreen != null) {
                    // スプラッシュを閉じる
                    splashScreen.close();
                }
            }
            catch (Exception ex) {
                // スプラッシュ関連のエラーは致命的ではないので継続してよい
                ex.printStackTrace(System.err);
            }
        });
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
