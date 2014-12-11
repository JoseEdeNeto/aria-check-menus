module.exports = (function (app_param, driver_param, fs_param) {
    var app = app_param,
        driver = driver_param,
        fs = fs_param;
    return {
        find_widget: function () {
            var result, promise_before, promise_after;
            promise_before = driver.captureScreen();
            result = app.find_widget.apply(app, arguments);
            promise_after = driver.captureScreen();

            return result.then(function (widgets) {
                if (widgets.length > 0) {
                    promise_before.then(function (image) {
                        fs.writeFile("widget_1_before.png", "image_before_base64");
                    });
                    promise_after.then(function (image) {
                        fs.writeFile("widget_1_after.png", "image_after_base64");
                    });
                }
                return widgets;
            });
        },
        find_all_widgets: function () {
            app.find_all_widgets();
        }
    };
});
