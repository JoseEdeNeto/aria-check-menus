module.exports = function (driver, webdriver) {
    var driver = driver,
        webdriver = webdriver,
        find_elements_based_on_visibility;

    find_elements_based_on_visibility = function (visibility) {
        return function () {
            return driver.findElements({css: "body *"}).then(function (elements) {
                if (elements.length === 0)
                    return elements;
                var promises = [];
                for (var i = 0; i < elements.length; i++) {
                    promises.push(elements[i].isDisplayed());
                };
                return webdriver.promise.all(promises).then(function (displays) {
                    var visible = [];
                    for (var i = 0; i < displays.length; i++) {
                        if ((visibility && displays[i]) || ( ! visibility && ! displays[i]))
                            visible.push(elements[i]);
                    };
                    return visible;
                });
            });
        };
    };

    return {
        get_visibles: find_elements_based_on_visibility(true),
        get_invisibles: find_elements_based_on_visibility(false)
    };
};
