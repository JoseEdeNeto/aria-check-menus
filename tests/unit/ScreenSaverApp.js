require("blanket")({pattern: "aria-check-menus/lib/"});

// promise stub
var Promise = function () {
    var self_args = arguments,
        self = this;
    if (self_args[0] && self_args[0].then)
        return self_args[0];
    return {
        then: function (callback) {
            return Promise(callback.apply(self, self_args));
        }
    };
};

var ScreenSaverApp = require("../../lib/ScreenSaverApp");

describe("ScreenSaverApp", function () {

    describe("#find_widget", function () {
        it("should call find_widget method in App", function (done) {
            var app_mock = {}, methods_calls = [], target_stub = { id: "something" },
                driver_mock = {}, captureScreen_returns = ["image_after_base64", "image_before_base64"],
                fs_mock = {}, fs_write_arguments = [],
                screen_app;
            driver_mock.captureScreen = function () {
                methods_calls.push("captureScreen");
                return Promise(captureScreen_returns.pop());
            };
            fs_mock.writeFile = function () {
                fs_write_arguments.push(arguments);
            };
            app_mock.find_widget = function (target) {
                target.should.be.equal(target_stub);
                methods_calls.push("find_widget");
                return Promise([{}]);
            };
            screen_app = ScreenSaverApp(app_mock, driver_mock, fs_mock);
            screen_app.should.have.property("find_widget");
            screen_app.find_widget(target_stub);
            methods_calls[0].should.be.equal("captureScreen");
            methods_calls[1].should.be.equal("find_widget");
            methods_calls[2].should.be.equal("captureScreen");
            fs_write_arguments.should.have.lengthOf(2);
            fs_write_arguments[0].should.have.lengthOf(2);
            fs_write_arguments[0][0].should.be.equal("widget_1_before.png");
            fs_write_arguments[0][1].should.be.equal("image_before_base64");
            fs_write_arguments[1][0].should.be.equal("widget_1_after.png");
            fs_write_arguments[1][1].should.be.equal("image_after_base64");
            done();
        });

        it("should return the same promise which is returned in app", function () {
            var app_mock = {}, driver_mock = {}, fs_mock = {}, target_stub = { id: "abobrinha" },
                screen_app, result;

            driver_mock.captureScreen = function () { return Promise([]); };
            fs_mock.writeFile = function () {};
            app_mock.find_widget = function () {
                var promise_mock = Promise([{ id: "a promise" }]);
                promise_mock.id = "a promise";
                return promise_mock;
            };
            screen_app = ScreenSaverApp(app_mock, driver_mock, fs_mock);
            result = screen_app.find_widget(target_stub);
            result.then(function (widgets) {
                widgets.should.have.property(0).and
                              .have.property("id").and
                              .be.equal("a promise");
            });
        });

        it("should not save widget image if no menus are found", function (done) {
            var app_mock = {}, driver_mock = {}, fs_mock = {},
                target_stub = { id: "abobrinha" }, no_widgets_stub,
                screen_app, result;

            driver_mock.captureScreen = function () { return Promise([]); };
            app_mock.find_widget = function () { return Promise(no_widgets_stub); };
            no_widgets_stub = [];
            fs_mock.writeFile = function () { "it should not be called".should.be.equal(""); };
            screen_app = ScreenSaverApp(app_mock, driver_mock, fs_mock),
            result = screen_app.find_widget(target_stub);
            done();
        });

        it("should save multiple images with different names for them", function (done) {
            var app_mock = {}, driver_mock = {}, fs_mock = {},
                target_stub = { id: "abobrinha" }, fs_stack_arguments = [],
                screen_app, result;

            driver_mock.captureScreen = function () { return Promise("imagedijfoiasjfoaaf base:64"); };
            app_mock.find_widget = function () { return Promise([{}]); };
            fs_mock.writeFile = function (filename, data) {
                fs_stack_arguments.push({
                    filename: filename,
                    data: data
                });
            };
            screen_app = ScreenSaverApp(app_mock, driver_mock, fs_mock),
            screen_app.find_widget(target_stub);
            screen_app.find_widget(target_stub);
            fs_stack_arguments.should.have.lengthOf(4);
            fs_stack_arguments[0].should.have.property("filename").and
                                 .be.equal("widget_1_before.png");
            fs_stack_arguments[0].should.have.property("data").and
                                 .be.equal("imagedijfoiasjfoaaf base:64");
            fs_stack_arguments[1].should.have.property("filename").and
                                 .be.equal("widget_1_after.png");
            fs_stack_arguments[1].should.have.property("data").and
                                 .be.equal("imagedijfoiasjfoaaf base:64");
            fs_stack_arguments[2].should.have.property("filename").and
                                 .be.equal("widget_2_before.png");
            fs_stack_arguments[2].should.have.property("data").and
                                 .be.equal("imagedijfoiasjfoaaf base:64");
            fs_stack_arguments[3].should.have.property("filename").and
                                 .be.equal("widget_2_after.png");
            fs_stack_arguments[3].should.have.property("data").and
                                 .be.equal("imagedijfoiasjfoaaf base:64");
            done();
        });

        it("should use folder parameter to save images", function (done) {
            var app_mock = {}, driver_mock = {}, fs_mock = {},
                target_stub = { id: "abobrinha" }, fs_stack_arguments = [],
                screen_app, result;

            driver_mock.captureScreen = function () { return Promise("imagedijfoiasjfoaaf base:64"); };
            app_mock.find_widget = function () { return Promise([{}]); };
            fs_mock.writeFile = function (filename, data) {
                fs_stack_arguments.push({
                    filename: filename,
                    data: data
                });
            };
            screen_app = ScreenSaverApp(app_mock, driver_mock, fs_mock, "some_folder"),
            screen_app.find_widget(target_stub);
            screen_app.find_widget(target_stub);
            fs_stack_arguments.should.have.lengthOf(4);
            fs_stack_arguments[0].should.have.property("filename").and
                                 .be.equal("some_folder/widget_1_before.png");
            fs_stack_arguments[1].should.have.property("filename").and
                                 .be.equal("some_folder/widget_1_after.png");
            fs_stack_arguments[2].should.have.property("filename").and
                                 .be.equal("some_folder/widget_2_before.png");
            fs_stack_arguments[3].should.have.property("filename").and
                                 .be.equal("some_folder/widget_2_after.png");
            done();
        });
    });

    describe("#find_all_widget", function () {
        it("should call find_all_widget method in App", function (done) {
            var app_mock = {}, methods_calls = [],
                screen_app = ScreenSaverApp(app_mock);
            app_mock.find_all_widgets = function () { methods_calls.push("find_all_widgets"); };
            screen_app.should.have.property("find_all_widgets");
            screen_app.find_all_widgets();
            methods_calls[0].should.be.equal("find_all_widgets");
            done();
        });
        it("should call find_all_widget in app which should call find_widget", function (done) {
            var app_mock = {},
                screen_app;
            app_mock.find_all_widgets = function () {
                this.find_widget();
            };
            app_mock.find_widget = function () {};
            screen_app = ScreenSaverApp(app_mock);
            screen_app.find_widget = function () { "ok".should.be.equal("ok"); done(); }
            screen_app.find_all_widgets();
        });
    });

});
