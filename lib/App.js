module.exports = function (driver, webdriver) {
    var driver = driver,
        webdriver = webdriver;
    return {
        get_visibles: function () {
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
                        if (displays[i])
                            visible.push(elements[i]);
                    };
                    return visible;
                });
            });
        },
        get_invisibles: function () {
            return driver.findElements({css: "body *"}).then(function (elements) {
                if (elements.length === 0)
                    return elements;
                var promises = [];
                for (var i = 0; i < elements.length; i++) {
                    promises.push(elements[i].isDisplayed());
                };
                return webdriver.promise.all(promises).then(function (displays) {
                    var invisible = [];
                    for (var i = 0; i < displays.length; i++) {
                        if ( ! displays[i])
                            invisible.push(elements[i]);
                    };
                    return invisible;
                });
            });
        }
    };
};
