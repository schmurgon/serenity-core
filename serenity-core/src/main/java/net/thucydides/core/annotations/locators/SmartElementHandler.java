package net.thucydides.core.annotations.locators;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class SmartElementHandler extends AbstractSingleItemHandler<WebElementFacade> {

    private static final String NO_SUITABLE_CONSTRUCTOR_FOUND_FMT2 = "No suitable constructor found.  "
            + "Expected:  %s(WebDriver, ElementLocator, long, long) or %s(WebDriver, ElementLocator, WebElement, long, long)";

    public SmartElementHandler(Class<?> interfaceType, ElementLocator locator, PageObject page) {
        super(WebElementFacade.class, interfaceType, locator, page);
    }

    @Override
    protected Object newElementInstance() throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        Object instance = null;

        if (ElementContructorForm.applicableConstructor(implementerClass).isPresent()) {
            Constructor constructor = ElementContructorForm.applicableConstructorFrom(implementerClass).get();
            switch (ElementContructorForm.applicableConstructor((implementerClass)).get()) {
                case WEBDRIVER_LOCATOR_SINGLE_TIMEOUT:
                    instance = constructor.newInstance(page.getDriver(), locator, page.getImplicitWaitTimeout().in(TimeUnit.MILLISECONDS));
                    break;
                case WEBDRIVER_LOCATOR_TWO_TIMEOUTS:
                    instance = constructor.newInstance(page.getDriver(), locator, page.getImplicitWaitTimeout().in(TimeUnit.MILLISECONDS), page.getWaitForTimeout().in(TimeUnit.MILLISECONDS));
                    break;
                case WEBDRIVER_ELEMENT_SINGLE_TIMEOUT:
                    instance = constructor.newInstance(page.getDriver(), locator, null, page.getImplicitWaitTimeout().in(TimeUnit.MILLISECONDS));
                    break;
                case WEBDRIVER_ELEMENT_TWO_TIMEOUTS:
                    instance = constructor.newInstance(page.getDriver(), locator, null, page.getImplicitWaitTimeout().in(TimeUnit.MILLISECONDS), page.getWaitForTimeout().in(TimeUnit.MILLISECONDS));
                    break;
            }
        }

        if (instance == null) {
            String className = implementerClass.getSimpleName();
            throw new RuntimeException(String.format(NO_SUITABLE_CONSTRUCTOR_FOUND_FMT2, className, className));
        }

//        try {
//            constructor = implementerClass.getConstructor(WebDriver.class, ElementLocator.class, long.class, long.class);
//            instance = constructor.newInstance(page.getDriver(), locator,
//                    page.getImplicitWaitTimeout().in(TimeUnit.MILLISECONDS),
//                    page.getWaitForTimeout().in(TimeUnit.MILLISECONDS));
//        } catch (NoSuchMethodException e) {
//            try {
//                constructor = implementerClass.getConstructor(WebDriver.class, ElementLocator.class, WebElement.class, long.class, long.class);
//                instance = constructor.newInstance(page.getDriver(), locator, null,
//                        page.getImplicitWaitTimeout().in(TimeUnit.MILLISECONDS),
//                        page.getWaitForTimeout().in(TimeUnit.MILLISECONDS));
//            } catch (NoSuchMethodException e1) {
//                String className = implementerClass.getSimpleName();
//                throw new RuntimeException(String.format(NO_SUITABLE_CONSTRUCTOR_FOUND_FMT2, className, className));
//            }
//        }
        return instance;
    }


}
