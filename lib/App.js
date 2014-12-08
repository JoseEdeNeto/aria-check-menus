module.exports = function (driver, webdriver) {
    var driver = driver, webdriver = webdriver,
        instance = {},
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

    instance._get_visibles = find_elements_based_on_visibility(true);
    instance._get_invisibles = find_elements_based_on_visibility(false);

    instance._hover = function (target) {
        var body = driver.findElement({css: "body"}),
            sequence = new webdriver.ActionSequence(driver)
                                    .mouseMove(body)
                                    .mouseMove(target)
                                    .perform();
    };

    instance.find_widget = function (target) {
        var invisibles, visibles_before, visibles_after;
        return instance._get_invisibles().then(function (inv) {
            invisibles = inv;
            return instance._get_visibles();
        }).then(function (before) {
            visibles_before = before;
            instance._hover(target);
            return instance._get_visibles();
        }).then(function (after) {
            var promises = [];
            visibles_after = after;
            for (var i = 0; i < invisibles.length; i++) {
                promises.push(invisibles[i].isDisplayed());
            };
            return webdriver.promise.all(promises);
        }).then(function (displays) {
            var widgets = [];
            for (var i = 0; i < displays.length; i++) {
                if (displays[i])
                    widgets.push(invisibles[i]);
            };
            //for (var i = 0; i < visibles_after.length; i++) {
            //    if(visibles_before.indexOf(visibles_after[i]) === -1)
            //        widgets.push(visibles_after[i]);
            //};
            return widgets;
        });
    };

    return instance;
};
