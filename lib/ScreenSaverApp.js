module.exports = (function (app_param, driver_param, fs_param) {
    var app = app_param,
        driver = driver_param,
        fs = fs_param,
        image_counter = 1;
    return {
        find_widget: function () {
            var result, promise_before, promise_after;
            promise_before = driver.captureScreen();
            result = app.find_widget.apply(app, arguments);
            promise_after = driver.captureScreen();

            return result.then(function (widgets) {
                if (widgets.length > 0) {
                    promise_before.then(function (image) {
                        fs.writeFile("widget_" + image_counter + "_before.png", image);
                    });
                    promise_after.then(function (image) {
                        fs.writeFile("widget_" + image_counter++ + "_after.png", image);
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