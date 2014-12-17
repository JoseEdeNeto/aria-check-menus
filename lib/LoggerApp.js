module.exports = (function (app_param, driver_param, fs_param, folder_param, console_param) {
    var app = app_param,
        driver = driver_param,
        console = console_param || {log: function () {}},
        fs = fs_param,
        folder = (folder_param ? (folder_param+"/") : ""),
        image_counter = 1,
        visible_widgets = null,
        app_find_widget = app.find_widget;
    app.get_visibles().then(function (visibles) {
        visible_widgets = visibles;
    });
    return {
        find_widget: function (activator) {
            var result, promise_before, promise_after;
            app._hover(driver.findElement({css: "body"}));
            promise_before = driver.takeScreenshot();
            result = app_find_widget.apply(app, arguments);
            promise_after = driver.takeScreenshot();

            return result.then(function (widgets) {
                if (widgets.length > 0) {
                    (function () {
                        var id = image_counter++;
                        promise_before.then(function (image) {
                            fs.writeFile(folder + "widget_" + id + "_before.png", image, "base64");
                        });
                        promise_after.then(function (image) {
                            fs.writeFile(folder + "widget_" + id + "_after.png", image, "base64");
                            console.log(id + " of " + visible_widgets.length + " visible elements inspected...");
                        });
                        activator.getOuterHtml().then(function (html) {
                            fs.writeFile(folder + "widget_" + id + "_activator_code.txt", html);
                        });
                        widgets[0].getOuterHtml().then(function (html) {
                            fs.writeFile(folder + "widget_" + id + "_code.txt", html);
                        });
                    }());
                }
                return widgets;
            });
        },
        find_all_widgets: function () {
            app.find_widget = this.find_widget;
            return app.find_all_widgets();
        }
    };
});
