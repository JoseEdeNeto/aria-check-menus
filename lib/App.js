module.exports = function (driver) {
    var driver = driver;
    return {
        get_invisibles: function () {
            return driver.findElements({css: "body *"}).then(function (elements) {
                if (elements.length === 0)
                    return elements;
                var promises = [];
                for (var i = 0; i < elements.length; i++) {
                    promises.push(elements[i].isDisplayed());
                };
                return driver.promise.all(promises).then(function (displays) {
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
