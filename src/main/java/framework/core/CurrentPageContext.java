package framework.core;

public class CurrentPageContext {

    private static ThreadLocal<BasePage> localCurrentPage  = new ThreadLocal<>();

    public static BasePage getCurrentPage() {
        return localCurrentPage.get();
    }

    public static void setCurrentPage(BasePage driverThreadLocal) {
        localCurrentPage.set(driverThreadLocal);
    }
}
