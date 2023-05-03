package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import guru.qa.data.Type;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
import java.util.stream.Stream;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class ParametrizedTestInKinopoisk {

    @BeforeAll
    static void setting() {
        open("https://www.kinopoisk.ru");
        Configuration.browserSize = "1920x1080";
    }

    @CsvFileSource(resources = "/testdata/searchResultsContainExpectedResults.csv")
    @ParameterizedTest (name = "Проверка соответсвия выдачи в поиске Кинопоиска по запросу {0} отображается текст {1}")
    void searchResultsContainExpectedResults(String testData, String expectedText) {
        $(byName("kp_query")).setValue(testData).pressEnter();
        $(".most_wanted").shouldHave(text(expectedText));
    }

    @ValueSource(strings = {
            "Batman", "Мира"
    })
    @ParameterizedTest (name = "Поисковая выдача кинопоиска отображает 1 результат по запросу {0}")
    void mostWantedSearchResultShouldBeOne (String testData) {
        $(byName("kp_query")).setValue(testData).pressEnter();
        $$(".most_wanted").shouldHave(CollectionCondition.size(1));
    }

    static Stream<Arguments> pageShouldContainAllOfGivenButtonsForGivenType() {
        return  Stream.of(
                Arguments.of(Type.Фильмы, List.of("Фильмы", "Онлайн-кинотеатр", "Жанры", "Страны", "Годы", "Критика", "Сериалы", "Сборы", "Премии", "Направления")),
                Arguments.of(Type.Сериалы, List.of("Фильмы", "Онлайн-кинотеатр", "Жанры", "Страны", "Годы", "Критика", "Сериалы", "Сборы", "Премии", "Направления"))
        );
    }

    @MethodSource
    @ParameterizedTest (name = "На странице для типа {0} отображается список кнопок {1}")
    void pageShouldContainAllOfGivenButtonsForGivenType(Type type, List<String> expectedButtons) {
        $$(".styles_sticky__mDnbt").find(text((type.name()))).click();
        $$("..styles_container__TJkuX  a").filter(visible).shouldHave(texts(expectedButtons));
    }

}
