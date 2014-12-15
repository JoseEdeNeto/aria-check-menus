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

    instance.get_visibles = find_elements_based_on_visibility(true);
    instance._get_invisibles = find_elements_based_on_visibility(false);

    instance._hover = function (target) {
        var body = driver.findElement({css: "body"}),
            sequence = new webdriver.ActionSequence(driver)
                                    .mouseMove(body)
                                    .mouseMove(target)
                                    .perform();
    };

    instance.find_widget = function (target) {
        var invisibles, visibles_before,
            visibles_after, widgets = [];
        return driver.executeScript(function () {
            window.mutationTargets = [];
            var observer = new MutationObserver(function (mutations) {
                mutations.forEach(function (mutation) {
                    if (mutation.addedNodes && mutation.addedNodes.length > 0)
                        window.mutationTargets.push(mutation.addedNodes[0]);
                });
            });
            observer.observe(document.body, {childList: true, subtree: true});
        }).then(function () {
            return instance._get_invisibles();
        }).then(function (inv) {
            var promises = [];
            invisibles = inv;
            instance._hover(target);
            for (var i = 0; i < invisibles.length; i++) {
                promises.push(invisibles[i].isDisplayed());
            };
            return webdriver.promise.all(promises);
        }).then(function (displays) {
            for (var i = 0; i < displays.length; i++) {
                if (displays[i])
                    widgets.push(invisibles[i]);
            };
            return widgets;
        }).then(function () {
            return driver.executeScript(function () {
                return window.mutationTargets.pop();
            });
        }).then(function (mutation_widget) {
            if (mutation_widget)
                widgets.push(mutation_widget);
            return widgets;
        }).then(function () {
            var biggest = 0, promises = [], new_widgets = [];
            for (var i = 0; i < widgets.length; i++) {
                promises.push(widgets[i].getOuterHtml());
            };
            return webdriver.promise.all(promises).then(function (outerHtmls) {
                for (var i = 1; i < outerHtmls.length; i++) {
                    if (outerHtmls[i].length > outerHtmls[biggest].length)
                        biggest = i;
                };
                if (widgets.length === 0)
                    return [];
                return [widgets[biggest]];
            });
        });
    };

    instance.find_all_widgets = function () {
        var visibles;
        return instance.get_visibles().then(function (v) {
            var promises = [];
            visibles = v;
            if (v.length === 0)
                return promises;
            for (var i = 0; i < visibles.length; i++) {
                promises.push(instance.find_widget(visibles[i]));
            };
            return webdriver.promise.all(promises);
        }).then(function (widgets) {
            var result = [];
            for (var i = 0; i < visibles.length; i++) {
                if (widgets[i].length != 0) {
                    result.push({
                        menu_activator: visibles[i],
                        menu: widgets[i]
                    });
                }
            };
            return result;
        });
    };

    return instance;
};
