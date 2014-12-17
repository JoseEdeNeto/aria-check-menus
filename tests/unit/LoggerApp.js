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

var LoggerApp = require("../../lib/LoggerApp");

describe("LoggerApp", function () {

    describe("#find_widget", function () {
        it("should call find_widget method in App", function (done) {
            var app_mock = {}, methods_calls = [], target_stub = { id: "something" },
                body_stub = { tagName: "body"}, driver_mock = {},
                captureScreen_returns = ["image_after_base64", "image_before_base64"],
                fs_mock = {}, fs_write_arguments = [], screen_app;
            driver_mock.takeScreenshot = function () {
                methods_calls.push("captureScreen");
                return Promise(captureScreen_returns.pop());
            };
            driver_mock.findElement = function (query) {
                query.should.have.property("css").and.be.equal("body");
                return body_stub;
            };
            fs_mock.writeFile = function () {
                fs_write_arguments.push(arguments);
            };
            app_mock.get_visibles = function () { return Promise([]); };
            app_mock._hover = function (target) {
                target.should.be.equal(body_stub);
                methods_calls.push("_hover");
            };
            app_mock.find_widget = function (target) {
                target.should.be.equal(target_stub);
                methods_calls.push("find_widget");
                return Promise([{
                    getOuterHtml: function () {
                        return Promise("<div>tooltip fragment</div>");
                    }
                }]);
            };
            target_stub.getOuterHtml = function () {
                return Promise("<span>tooltip activator</span>");
            };
            screen_app = LoggerApp(app_mock, driver_mock, fs_mock);
            screen_app.should.have.property("find_widget");
            screen_app.find_widget(target_stub);
            methods_calls[0].should.be.equal("_hover");
            methods_calls[1].should.be.equal("captureScreen");
            methods_calls[2].should.be.equal("find_widget");
            methods_calls[3].should.be.equal("captureScreen");
            fs_write_arguments.should.have.lengthOf(4);
            fs_write_arguments[0].should.have.lengthOf(3);
            fs_write_arguments[0][0].should.be.equal("widget_1_before.png");
            fs_write_arguments[0][1].should.be.equal("image_before_base64");
            fs_write_arguments[0][2].should.be.equal("base64");
            fs_write_arguments[1].should.have.lengthOf(3);
            fs_write_arguments[1][0].should.be.equal("widget_1_after.png");
            fs_write_arguments[1][1].should.be.equal("image_after_base64");
            fs_write_arguments[1][2].should.be.equal("base64");
            fs_write_arguments[2].should.have.lengthOf(2);
            fs_write_arguments[2][0].should.be.equal("widget_1_activator_code.txt");
            fs_write_arguments[2][1].should.be.equal("<span>tooltip activator</span>");
            fs_write_arguments[3].should.have.lengthOf(2);
            fs_write_arguments[3][0].should.be.equal("widget_1_code.txt");
            fs_write_arguments[3][1].should.be.equal("<div>tooltip fragment</div>");
            done();
        });

        it("should return the same promise which is returned in app", function () {
            var app_mock = {}, driver_mock = {}, fs_mock = {},
                target_stub = { id: "abobrinha", getOuterHtml: function () {return Promise({});} },
                screen_app, result;

            driver_mock.takeScreenshot = function () { return Promise([]); };
            driver_mock.findElement = function (query) { return "body"; };
            fs_mock.writeFile = function () {};
            app_mock.get_visibles = function () { return Promise([]); };
            app_mock._hover = function (target) { target.should.be.equal("body"); };
            app_mock.find_widget = function () {
                var promise_mock = Promise([{ id: "a promise", getOuterHtml: function () {return Promise({});}}]);
                promise_mock.id = "a promise";
                return promise_mock;
            };
            screen_app = LoggerApp(app_mock, driver_mock, fs_mock);
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

            driver_mock.takeScreenshot = function () { return Promise([]); };
            driver_mock.findElement = function (query) { return "body"; };
            app_mock.get_visibles = function () { return Promise([]); };
            app_mock._hover = function (target) { target.should.be.equal("body"); };
            app_mock.find_widget = function () { return Promise(no_widgets_stub); };
            no_widgets_stub = [];
            fs_mock.writeFile = function () { "it should not be called".should.be.equal(""); };
            screen_app = LoggerApp(app_mock, driver_mock, fs_mock),
            result = screen_app.find_widget(target_stub);
            done();
        });

        it("should save multiple images with different names for them", function (done) {
            var app_mock = {}, driver_mock = {}, fs_mock = {},
                target_stub = { id: "abobrinha", getOuterHtml: function () {return Promise("activator");} },
                fs_stack_arguments = [],
                screen_app, result, screenshot_promises;

            screenshot_promises = Promise("imagedijfoiasjfoaaf base:64");
            driver_mock.takeScreenshot = function () { return screenshot_promises; };
            driver_mock.findElement = function (query) { return "body"; };
            app_mock.get_visibles = function () { return Promise([]); };
            app_mock._hover = function (target) { target.should.be.equal("body"); };
            app_mock.find_widget = function () {
                return Promise([{getOuterHtml: function () {return Promise("widget"); }}]); };
            fs_mock.writeFile = function (filename, data) {
                fs_stack_arguments.push({
                    filename: filename,
                    data: data
                });
            };
            screen_app = LoggerApp(app_mock, driver_mock, fs_mock),
            screen_app.find_widget(target_stub);
            screen_app.find_widget(target_stub);
            fs_stack_arguments.should.have.lengthOf(8);
            fs_stack_arguments[0].should.have.property("filename").and
                                 .be.equal("widget_1_before.png");
            fs_stack_arguments[0].should.have.property("data").and
                                 .be.equal("imagedijfoiasjfoaaf base:64");
            fs_stack_arguments[1].should.have.property("filename").and
                                 .be.equal("widget_1_after.png");
            fs_stack_arguments[1].should.have.property("data").and
                                 .be.equal("imagedijfoiasjfoaaf base:64");

            fs_stack_arguments[2].should.have.property("filename").and
                                 .be.equal("widget_1_activator_code.txt");
            fs_stack_arguments[2].should.have.property("data").and
                                 .be.equal("activator");
            fs_stack_arguments[3].should.have.property("filename").and
                                 .be.equal("widget_1_code.txt");
            fs_stack_arguments[3].should.have.property("data").and
                                 .be.equal("widget");

            fs_stack_arguments[4].should.have.property("filename").and
                                 .be.equal("widget_2_before.png");
            fs_stack_arguments[4].should.have.property("data").and
                                 .be.equal("imagedijfoiasjfoaaf base:64");
            fs_stack_arguments[5].should.have.property("filename").and
                                 .be.equal("widget_2_after.png");
            fs_stack_arguments[5].should.have.property("data").and
                                 .be.equal("imagedijfoiasjfoaaf base:64");

            fs_stack_arguments[6].should.have.property("filename").and
                                 .be.equal("widget_2_activator_code.txt");
            fs_stack_arguments[6].should.have.property("data").and
                                 .be.equal("activator");
            fs_stack_arguments[7].should.have.property("filename").and
                                 .be.equal("widget_2_code.txt");
            fs_stack_arguments[7].should.have.property("data").and
                                 .be.equal("widget");
            done();
        });

        it("should save multiple images with different names for them (async)", function (done) {
            var app_mock = {}, driver_mock = {}, fs_mock = {},
                target_stub = { id: "abobrinha", getOuterHtml: function () {return Promise("activator");} },
                fs_stack_arguments = [],
                screen_app, result, screenshot_promises;

            screenshot_promises = Promise("imagedijfoiasjfoaaf base:64");
            screenshot_promises.true_then = screenshot_promises.then;
            screenshot_promises.then = function () {
                var self = this,
                    then_arguments = arguments;
                setTimeout(function () {
                    self.true_then.apply(self, then_arguments);
                }, 1);
            };
            driver_mock.takeScreenshot = function () { return screenshot_promises; };
            driver_mock.findElement = function (query) { return "body"; };
            app_mock.get_visibles = function () { return Promise([]); };
            app_mock._hover = function (target) { target.should.be.equal("body"); };
            app_mock.find_widget = function () {
                return Promise([{getOuterHtml: function () {return Promise("widget"); }}]); };
            fs_mock.writeFile = function (filename, data) {
                fs_stack_arguments.push({
                    filename: filename,
                    data: data
                });
            };
            screen_app = LoggerApp(app_mock, driver_mock, fs_mock),
            screen_app.find_widget(target_stub);
            screen_app.find_widget(target_stub);
            setTimeout(function () {
                fs_stack_arguments.should.have.lengthOf(8);
                fs_stack_arguments[4].should.have.property("filename").and
                                     .be.equal("widget_1_before.png");
                fs_stack_arguments[4].should.have.property("data").and
                                     .be.equal("imagedijfoiasjfoaaf base:64");
                fs_stack_arguments[5].should.have.property("filename").and
                                     .be.equal("widget_1_after.png");
                fs_stack_arguments[5].should.have.property("data").and
                                     .be.equal("imagedijfoiasjfoaaf base:64");

                fs_stack_arguments[0].should.have.property("filename").and
                                     .be.equal("widget_1_activator_code.txt");
                fs_stack_arguments[0].should.have.property("data").and
                                     .be.equal("activator");
                fs_stack_arguments[1].should.have.property("filename").and
                                     .be.equal("widget_1_code.txt");
                fs_stack_arguments[1].should.have.property("data").and
                                     .be.equal("widget");

                fs_stack_arguments[6].should.have.property("filename").and
                                     .be.equal("widget_2_before.png");
                fs_stack_arguments[6].should.have.property("data").and
                                     .be.equal("imagedijfoiasjfoaaf base:64");
                fs_stack_arguments[7].should.have.property("filename").and
                                     .be.equal("widget_2_after.png");
                fs_stack_arguments[7].should.have.property("data").and
                                     .be.equal("imagedijfoiasjfoaaf base:64");

                fs_stack_arguments[2].should.have.property("filename").and
                                     .be.equal("widget_2_activator_code.txt");
                fs_stack_arguments[2].should.have.property("data").and
                                     .be.equal("activator");
                fs_stack_arguments[3].should.have.property("filename").and
                                     .be.equal("widget_2_code.txt");
                fs_stack_arguments[3].should.have.property("data").and
                                     .be.equal("widget");
                done();
            }, 1);
        });

        it("should use folder parameter to save images", function (done) {
            var app_mock = {}, driver_mock = {}, fs_mock = {},
                target_stub = { id: "abobrinha", getOuterHtml: function () {return Promise("activator");} },
                fs_stack_arguments = [], screen_app, result;

            driver_mock.takeScreenshot = function () { return Promise("imagedijfoiasjfoaaf base:64"); };
            driver_mock.findElement = function (query) { return "body"; };
            app_mock.get_visibles = function () { return Promise([]); };
            app_mock._hover = function (target) { target.should.be.equal("body"); };
            app_mock.find_widget = function () { return Promise(
                [{getOuterHtml: function () {return Promise("widget");}}]); };
            fs_mock.writeFile = function (filename, data) {
                fs_stack_arguments.push({
                    filename: filename,
                    data: data
                });
            };
            screen_app = LoggerApp(app_mock, driver_mock, fs_mock, "some_folder"),
            screen_app.find_widget(target_stub);
            screen_app.find_widget(target_stub);
            fs_stack_arguments.should.have.lengthOf(8);
            fs_stack_arguments[0].should.have.property("filename").and
                                 .be.equal("some_folder/widget_1_before.png");
            fs_stack_arguments[1].should.have.property("filename").and
                                 .be.equal("some_folder/widget_1_after.png");
            fs_stack_arguments[2].should.have.property("filename").and
                                 .be.equal("some_folder/widget_1_activator_code.txt");
            fs_stack_arguments[3].should.have.property("filename").and
                                 .be.equal("some_folder/widget_1_code.txt");
            fs_stack_arguments[4].should.have.property("filename").and
                                 .be.equal("some_folder/widget_2_before.png");
            fs_stack_arguments[5].should.have.property("filename").and
                                 .be.equal("some_folder/widget_2_after.png");
            fs_stack_arguments[6].should.have.property("filename").and
                                 .be.equal("some_folder/widget_2_activator_code.txt");
            fs_stack_arguments[7].should.have.property("filename").and
                                 .be.equal("some_folder/widget_2_code.txt");
            done();
        });

        it("should log the percentage of completion for a webpage", function (done) {
            var app_mock = {}, driver_mock = {}, fs_mock = {}, console_mock = {},
                target_stub = { id: "abobrinha", getOuterHtml: function () {return Promise("activator");} },
                console_log_arguments = [], screen_app, result, screenshot_promises;

            driver_mock.takeScreenshot = function () { return Promise("abobrinha base64"); };
            driver_mock.findElement = function (query) { return "body"; };
            app_mock.get_visibles = function () { return Promise([1, 2, 3, 4, 5]); };
            app_mock._hover = function (target) { target.should.be.equal("body"); };
            app_mock.find_widget = function () {
                return Promise([{getOuterHtml: function () {return Promise("widget"); }}]); };
            fs_mock.writeFile = function (filename, data) { };
            console_mock.log = function (message) { console_log_arguments.push(message); };
            screen_app = LoggerApp(app_mock, driver_mock, fs_mock, "", console_mock),
            screen_app.find_widget(target_stub);
            screen_app.find_widget(target_stub);
            console_log_arguments.should.have.lengthOf(2);
            console_log_arguments[0].should.be.equal("1 of 5 visible elements inspected...");
            console_log_arguments[1].should.be.equal("2 of 5 visible elements inspected...");
            done();
        });

        it("should log the percentage of completion for a webpage after find_widget promise", function (done) {
            var app_mock = {}, driver_mock = {}, fs_mock = {}, console_mock = {},
                target_stub = { id: "abobrinha", getOuterHtml: function () {return Promise("activator");} },
                console_log_arguments = [], screen_app, result, screenshot_promises, screenshot_counter = 0;

            driver_mock.takeScreenshot = function () {
                var screenshot_promise = Promise("abobrinha base64");
                screenshot_promise.true_then = screenshot_promise.then;
                screenshot_promise.then = function () {
                    var self = this;
                    screenshot_counter++;
                    if (screenshot_counter === 1)
                        console_log_arguments.should.have.lengthOf(0);
                    if (screenshot_counter === 3)
                        console_log_arguments.should.have.lengthOf(1);
                    screenshot_promise.true_then.apply(self, arguments);
                };
                return screenshot_promise;
            };
            driver_mock.findElement = function (query) { return "body"; };
            app_mock.get_visibles = function () { return Promise([2, 3, 4, 5]); };
            app_mock._hover = function (target) { target.should.be.equal("body"); };
            app_mock.find_widget = function () {
                return Promise([{getOuterHtml: function () {return Promise("widget"); }}]); };
            fs_mock.writeFile = function (filename, data) { };
            console_mock.log = function (message) { console_log_arguments.push(message); };
            screen_app = LoggerApp(app_mock, driver_mock, fs_mock, "", console_mock),
            screen_app.find_widget(target_stub);
            screen_app.find_widget(target_stub);
            console_log_arguments.should.have.lengthOf(2);
            console_log_arguments[0].should.be.equal("1 of 4 visible elements inspected...");
            console_log_arguments[1].should.be.equal("2 of 4 visible elements inspected...");
            done();
        });
    });

    describe("#find_all_widget", function () {
        it("should call find_all_widget method in App", function (done) {
            var app_mock = {}, methods_calls = [],
                screen_app, result;
            app_mock.get_visibles = function () { return Promise([]); };
            app_mock.find_all_widgets = function () {
                methods_calls.push("find_all_widgets");
                return "something";
            };
            screen_app = LoggerApp(app_mock);
            screen_app.should.have.property("find_all_widgets");
            result = screen_app.find_all_widgets();
            methods_calls[0].should.be.equal("find_all_widgets");
            result.should.be.equal("something");
            done();
        });
        it("should call find_all_widget in app which should call find_widget", function (done) {
            var app_mock = {},
                screen_app;
            app_mock.get_visibles = function () { return Promise([]); };
            app_mock.find_all_widgets = function () {
                this.find_widget();
            };
            app_mock.find_widget = function () {};
            screen_app = LoggerApp(app_mock);
            screen_app.find_widget = function () { "ok".should.be.equal("ok"); done(); }
            screen_app.find_all_widgets();
        });
    });

});
