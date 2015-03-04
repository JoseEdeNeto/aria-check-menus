require("blanket")({pattern: "aria-check-menus/lib/"});
var App = require("../../lib/App");

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

describe("App", function () {

    describe("#_get_invisible", function () {
        it("should look for elements using driver", function (done) {
            var driver_mock = {},
                app = App(driver_mock),
                expected_elements = [],
                result_promise;
            driver_mock.findElements = function () {
                arguments.length.should.be.equal(1);
                arguments[0].should.be.eql({css: "body *"});
                return Promise(expected_elements);
            };
            result_promise = app._get_invisibles();
            result_promise.then(function (result) {
                result.should.have.length(0);
                result.should.be.equal(expected_elements);
                done();
            });
        });

        it("should find invisible elements in the list", function (done) {
            var driver_mock = {}, webdriver = {},
                app = App(driver_mock, webdriver),
                expected_elements = [],
                result_promise;
            driver_mock.findElements = function () {
                return Promise(expected_elements);
            };
            webdriver.promise = {};
            webdriver.promise.all = function () {
                arguments.should.have.lengthOf(1);
                arguments[0].should.have.lengthOf(2);
                arguments[0][0].should.have.property("id", "p1");
                arguments[0][1].should.have.property("id", "p2");
                return Promise([true, false]);
            };
            expected_elements[0] = {
                id: "element1", isDisplayed: function () { var p = Promise(true); p.id = "p1"; return p;} };
            expected_elements[1] = {
                id: "element2", isDisplayed: function () { var p = Promise(false); p.id = "p2"; return p;} };
            result_promise = app._get_invisibles();
            result_promise.then(function (result) {
                result.should.have.length(1);
                result.should.containEql(expected_elements[1]);
                result.should.not.containEql(expected_elements[0]);
                done();
            });
        });
    });

    describe("#get_visible", function () {
        it("should look for elements using driver", function (done) {
            var driver_mock = {},
                app = App(driver_mock),
                expected_elements = [],
                result_promise;
            driver_mock.findElements = function () {
                arguments.length.should.be.equal(1);
                arguments[0].should.be.eql({css: "body *"});
                return Promise(expected_elements);
            };
            result_promise = app.get_visibles();
            result_promise.then(function (result) {
                result.should.have.length(0);
                result.should.be.equal(expected_elements);
                done();
            });
        });

        it("should find visible elements in the list", function (done) {
            var driver_mock = {}, webdriver = {},
                app = App(driver_mock, webdriver),
                expected_elements = [],
                result_promise;
            driver_mock.findElements = function () {
                return Promise(expected_elements);
            };
            webdriver.promise = {};
            webdriver.promise.all = function () {
                arguments.should.have.lengthOf(1);
                arguments[0].should.have.lengthOf(2);
                arguments[0][0].should.have.property("id", "p1");
                arguments[0][1].should.have.property("id", "p2");
                return Promise([true, false]);
            };
            expected_elements[0] = {
                id: "element1", isDisplayed: function () { var p = Promise(true); p.id = "p1"; return p;} };
            expected_elements[1] = {
                id: "element2", isDisplayed: function () { var p = Promise(false); p.id = "p2"; return p;} };
            result_promise = app.get_visibles();
            result_promise.then(function (result) {
                result.should.have.length(1);
                result.should.containEql(expected_elements[0]);
                result.should.not.containEql(expected_elements[1]);
                done();
            });
        });
    });

    describe("#_hover", function () {
        it("should call hover in an ActionSequence", function () {
            var driver_mock, webdriver_mock, ActionSequenceMock, body_element_stub,
                web_element_stub, app, hover_target, methods_arguments = [];
            body_element_stub = {"id": "stub body"};
            driver_mock = {
                id: "driver_mock",
                findElement: function (selector) {
                    selector.should.be.eql({css: "body"});
                    return body_element_stub;
                }
            };
            ActionSequenceMock = function (){ methods_arguments.push(
                {method: "constructor", arguments: arguments}); };
            ActionSequenceMock.prototype.mouseMove = function () { methods_arguments.push(
                {method: "mouseMove", arguments: arguments}); return this; }
            ActionSequenceMock.prototype.perform = function () { methods_arguments.push(
                {method: "perform", arguments: arguments}); return this; }
            webdriver_mock = {
                ActionSequence: ActionSequenceMock
            };
            web_element_stub = { id: "some_target_element" };
            app = App(driver_mock, webdriver_mock);
            app._hover(web_element_stub);
            methods_arguments.should.have.lengthOf(4);
            methods_arguments[0].method.should.be.equal("constructor");
            methods_arguments[0].arguments[0].should.be.equal(driver_mock);
            methods_arguments[1].method.should.be.equal("mouseMove");
            methods_arguments[1].arguments[0].should.be.equal(body_element_stub);
            methods_arguments[2].method.should.be.equal("mouseMove");
            methods_arguments[2].arguments[0].should.be.equal(web_element_stub);
            methods_arguments[3].method.should.be.equal("perform");
            methods_arguments[3].arguments.should.have.lengthOf(0);
        });
    });

    describe("#find_widget", function () {
        it("should _hover an element and check for invisibles which became visible", function (done) {
            var app, driver_mock = {}, webdriver_mock = { promise: {} }, target_stub = {id: "abobrinha1"},
                method_calls = [], invisibles_stub = [], result_promise;

            invisibles_stub[0] = { id: "1", isDisplayed: function () { return { fakePromise: 1 } },
                getOuterHtml: function () { return Promise("<span></span>"); } };
            invisibles_stub[1] = { id: "2", isDisplayed: function () { return { fakePromise: 2 } },
                getOuterHtml: function () { return Promise("<span></span>"); } };
            invisibles_stub[2] = { id: "3", isDisplayed: function () { return { fakePromise: 3 } },
                getOuterHtml: function () { return Promise("<span>bigger one should be returned</span>"); } };
            invisibles_stub[3] = { id: "4", isDisplayed: function () { return { fakePromise: 4 } },
                getOuterHtml: function () { return Promise("<span></span>"); } };

            app = App(driver_mock, webdriver_mock);

            webdriver_mock.promise.all = function (promises) {
                promises.should.have.lengthOf(4);
                promises[0].fakePromise.should.be.equal(1);
                promises[1].fakePromise.should.be.equal(2);
                promises[2].fakePromise.should.be.equal(3);
                promises[3].fakePromise.should.be.equal(4);
                webdriver_mock.promise.all = function () {
                    return Promise(["<span></span>", "<span>bigger one should be returned</span>"]);
                };
                return Promise([false, true, true, false]);
            };
            driver_mock.findElements = function () { return Promise([]); };
            driver_mock.executeScript = function (callback) { return Promise(); };

            // mocking private methods
            app._hover = function () { method_calls.push({method: "_hover", arguments: arguments}); };
            app._get_invisibles = function () {
                return Promise(invisibles_stub);
            };

            result_promise = app.find_widget(target_stub);
            method_calls.should.have.lengthOf(1);
            method_calls[0].method.should.be.equal("_hover");
            method_calls[0].should.have.property("arguments").with.length(1);
            method_calls[0].arguments[0].should.be.equal(target_stub);
            result_promise.then(function (widgets) {
                widgets.should.have.lengthOf(1);
                widgets[0].should.have.property("id", "3");
                done();
            });
        });

        it("should _hover an element and look for elements which did not exist before", function (done) {
            var app, driver_mock = {}, webdriver_mock = {}, target_stub = {}, execute_script_called = 0;
            app = App(driver_mock, webdriver_mock);

            webdriver_mock.promise = {
                all: function (promises) {
                    promises.should.have.lengthOf(0);
                    webdriver_mock.promise.all = function (promises) {
                        promises.should.have.lengthOf(1);
                        return Promise(["aoioioioio"]);
                    };
                    return Promise([]);
                }
            };
            app._hover = function () {};
            app._get_invisibles = function () { return Promise([]); };
            driver_mock.executeScript = function (callback) {
                execute_script_called++;
                callback.toString().should.containDeep("MutationObserver");
                callback.toString().should.containDeep("mutation.addedNodes[0].nodeType === 1");
                callback.toString().should.containDeep(
                    "observer.observe(document.body, {childList: true, subtree: true})");
                callback.toString().should.containDeep(
                    "for (var i = 0; i < 10000; i++) { clearTimeout(i); clearInterval(i); };");
                callback.toString().should.containDeep(
                    "window.setInterval = function () {};");
                driver_mock.executeScript = function (callback) {
                    execute_script_called++;
                    callback.should.containDeep(
                        "var mutation_widget = document.querySelectorAll(\".mutation_widget\");" +
                        "for (var i = 0; i < mutation_widget.length; i++)" +
                        "    mutation_widget[i].className = mutation_widget[i].className.split(\"mutation_widget\").join(\"\");"
                    );
                    return Promise([]);
                };
                return Promise([]);
            };
            driver_mock.findElements = function (query) {
                query.should.have.property("css").and.be.equal(".mutation_widget");
                execute_script_called.should.be.equal(1);
                return Promise([{id: "mutationObserved", getOuterHtml: function () { return Promise("aio"); }}]);
            };

            app.find_widget(target_stub).then(function (widgets) {
                widgets.should.have.property(0).and
                              .have.property("id").and
                              .be.equal("mutationObserved");
                execute_script_called.should.be.equal(2);
                done();
            });
        });

        it("should _hover an element and look for multiple elements which did not exist before", function (done) {
            var app, driver_mock = {}, webdriver_mock = {}, target_stub = {};
            app = App(driver_mock, webdriver_mock);

            webdriver_mock.promise = {
                all: function (promises) {
                    return Promise([]);
                }
            };
            app._hover = function () {};
            app._get_invisibles = function () { return Promise([]); };
            driver_mock.executeScript = function (callback) {
                return Promise([]);
            };
            driver_mock.findElements = function (query) {
                query.should.have.property("css").and.be.equal(".mutation_widget");
                return Promise([{id: "mutationObserved", getOuterHtml: function () { return Promise("aio"); }}]);
            };

            app.find_widget(target_stub).then(function (widgets) {
                widgets.should.have.property(0).and
                              .have.property("id").and
                              .be.equal("mutationObserved");
            });

            driver_mock.executeScript = function (callback) {
                return Promise([]);
            };
            driver_mock.findElements = function (query) {
                query.should.have.property("css").and.be.equal(".mutation_widget");
                return Promise([{id: "mutationObserved", getOuterHtml: function () { return Promise("aio"); }}]);
            };

            app.find_widget(target_stub).then(function (widgets) {
                widgets.should.have.property(0).and
                              .have.property("id").and
                              .be.equal("mutationObserved");
                done();
            });
        });

        it("should not break if no widget elements are found", function (done) {
            var app, driver_mock = {}, webdriver_mock = { promise: {}, WebElement: {} },
                target_stub = {id: "abobrinha1"}, method_calls = [], invisibles_stub = [],
                result_promise, visible_stubs_stack = [], promise_all_stack = [];

            app = App(driver_mock, webdriver_mock);

            promise_all_stack = [Promise("abobrinha"),
                                 Promise([true, false, false, true]),
                                 Promise([])];
            webdriver_mock.promise.all = function (promises) { return promise_all_stack.pop(); };
            webdriver_mock.WebElement.equals = function (a, b) { return Promise(a.id === b.id); };
            driver_mock.findElements = function () { return Promise([]); };
            driver_mock.executeScript = function (callback) { return Promise(); };

            // mocking private methods
            app._hover = function () { method_calls.push({method: "_hover", arguments: arguments}); };
            app._get_invisibles = function () { return Promise(invisibles_stub); };

            result_promise = app.find_widget(target_stub);
            result_promise.then(function (widgets) {
                widgets.should.have.lengthOf(0);
                done();
            });
        });

        it("should call _get_invisibles only once", function (done) {
            var app, driver_mock = {}, webdriver_mock = { promise: {}, WebElement: {} },
                target_stub = {id: "abobrinha1"}, method_calls = [], invisibles_stub = [],
                result_promise, visible_stubs_stack = [];

            app = App(driver_mock, webdriver_mock);

            webdriver_mock.promise.all = function (promises) { return Promise([]); };
            driver_mock.findElements = function () { return Promise([]); };
            driver_mock.executeScript = function (callback) { return Promise(); };

            // mocking private methods
            app._hover = function () { method_calls.push({method: "_hover", arguments: arguments}); };
            app._get_invisibles = function () {
                this._get_invisibles = function () {
                    "".should.be.equal("it should not pass through here");
                }
                return Promise(invisibles_stub);
            };

            app.find_widget(target_stub);
            app.find_widget(target_stub);
            done();
        });
    });

    describe("#find_all_widgets", function () {
        it("should get all visibles and do nothing if nothing is returned", function (done) {
            var driver_mock = {}, webdriver_mock = {},
                app = App(driver_mock, webdriver_mock),
                get_visibles_promise;

            get_visibles_promise = Promise([]);
            app.get_visibles = function () {
                return get_visibles_promise;
            };
            app.find_all_widgets().then(function (result) {
                result.should.be.an.Array.with.lengthOf(0);
                done();
            });

        });

        it("should call get all visible elements and call find_widget", function (done) {
            var driver_mock = {}, webdriver_mock = {},
                app = App(driver_mock, webdriver_mock),
                get_visibles_promise, find_widget_arguments = [],
                find_widget_results = ["promise 2", "promise 1"];

            webdriver_mock.promise = {};
            webdriver_mock.promise.all = function (promises) {
                promises.should.have.lengthOf(2);
                promises[0].should.be.equal("promise 1");
                promises[1].should.be.equal("promise 2");
                return Promise(["widget 1", "widget 2"]);
            };
            get_visibles_promise = Promise(["visible 1", "visible 2"]);
            app.find_widget = function (target) {
                find_widget_arguments.push(arguments);
                return find_widget_results.pop();
            };
            app.get_visibles = function () {
                return get_visibles_promise;
            };
            app.find_all_widgets().then(function (result) {
                find_widget_arguments.should.have.lengthOf(2);
                find_widget_arguments[0][0].should.be.equal("visible 1");
                find_widget_arguments[1][0].should.be.equal("visible 2");

                result.should.have.lengthOf(2);
                result[0].should.have.property("menu_activator", "visible 1");
                result[0].should.have.property("menu", "widget 1");
                result[1].should.have.property("menu_activator", "visible 2");
                result[1].should.have.property("menu", "widget 2");
                done();
            });
        });

        it("should not return elements which did not present a dropdown component", function (done) {
            var driver_mock = {}, webdriver_mock = {},
                app = App(driver_mock, webdriver_mock),
                get_visibles_promise, find_widget_arguments = [],
                find_widget_results = ["promise 2", "promise 1"];

            webdriver_mock.promise = {};
            webdriver_mock.promise.all = function (promises) {
                promises.should.have.lengthOf(2);
                return Promise([[], []]);
            };
            get_visibles_promise = Promise(["visible 1", "visible 2"]);
            app.find_widget = function (target) {
                find_widget_arguments.push(arguments);
                return find_widget_results.pop();
            };
            app.get_visibles = function () {
                return get_visibles_promise;
            };
            app.find_all_widgets().then(function (result) {
                find_widget_arguments.should.have.lengthOf(2);

                result.should.have.lengthOf(0);
                done();
            });
        });

    });

});
