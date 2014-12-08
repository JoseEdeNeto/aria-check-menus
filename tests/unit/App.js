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

            invisibles_stub[0] = { id: "1", isDisplayed: function () { return { fakePromise: 1 } }};
            invisibles_stub[1] = { id: "2", isDisplayed: function () { return { fakePromise: 2 } }};
            invisibles_stub[2] = { id: "3", isDisplayed: function () { return { fakePromise: 3 } }};
            invisibles_stub[3] = { id: "4", isDisplayed: function () { return { fakePromise: 4 } }};

            app = App(driver_mock, webdriver_mock);

            webdriver_mock.promise.all = function (promises) {
                promises.should.have.lengthOf(4);
                promises[0].fakePromise.should.be.equal(1);
                promises[1].fakePromise.should.be.equal(2);
                promises[2].fakePromise.should.be.equal(3);
                promises[3].fakePromise.should.be.equal(4);
                webdriver_mock.promise.all = function () {return [];}
                return Promise([false, true, true, false]);
            };
            driver_mock.findElements = function () { return Promise([]); };

            // mocking private methods
            app._hover = function () { method_calls.push({method: "_hover", arguments: arguments}); };
            app._get_invisibles = function () { return Promise(invisibles_stub); };

            result_promise = app.find_widget(target_stub);
            method_calls.should.have.lengthOf(1);
            method_calls[0].method.should.be.equal("_hover");
            method_calls[0].should.have.property("arguments").with.length(1);
            method_calls[0].arguments[0].should.be.equal(target_stub);
            result_promise.then(function (widgets) {
                widgets.should.have.lengthOf(2);
                widgets[0].should.have.property("id", "2");
                widgets[1].should.have.property("id", "3");
                done();
            });
        });

        it("should _hover an element and look for elements which did not exist before", function (done) {
            var app, driver_mock = {}, webdriver_mock = { promise: {}, WebElement: {} },
                target_stub = {id: "abobrinha1"}, method_calls = [], invisibles_stub = [],
                result_promise, visible_stubs_stack = [], promise_all_stack = [],
                visible_stub_1 = [
                    {id: 1},
                    {id: 2}
                ], visible_stub_2 = [
                    {id: 1},
                    {id: 2},
                    {id: 3}
                ];

            app = App(driver_mock, webdriver_mock);

            promise_all_stack = [Promise([true, false, false, true, false, false]), Promise([])];
            webdriver_mock.promise.all = function (promises) { return promise_all_stack.pop(); };
            webdriver_mock.WebElement.equals = function (a, b) { return Promise(a.id === b.id); };
            driver_mock.findElements = function () {
                method_calls.push({method: "driver.findElements", arguments: arguments});
                return Promise(visible_stubs_stack.pop());
            };

            // mocking private methods
            app._hover = function () { method_calls.push({method: "_hover", arguments: arguments}); };
            app._get_invisibles = function () { return Promise(invisibles_stub); };
            visible_stubs_stack = [visible_stub_2, visible_stub_1];

            result_promise = app.find_widget(target_stub);
            method_calls.should.have.lengthOf(3);
            method_calls[0].should.have.property("method", "driver.findElements");
            method_calls[0].should.have.property("arguments").with.lengthOf(1);
            method_calls[0].arguments[0].should.have.property("css", "body *");
            method_calls[1].method.should.be.equal("_hover");
            method_calls[1].should.have.property("arguments").with.length(1);
            method_calls[1].arguments[0].should.be.equal(target_stub);
            method_calls[2].should.have.property("method", "driver.findElements");
            method_calls[2].should.have.property("arguments").with.lengthOf(1);
            method_calls[2].arguments[0].should.have.property("css", "body *");
            result_promise.then(function (widgets) {
                widgets.should.have.lengthOf(1);
                widgets[0].should.have.property("id", 3);
                done();
            });
        });
    });

});