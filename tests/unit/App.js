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

    describe("#get_invisible", function () {
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
            result_promise = app.get_invisibles();
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
            result_promise = app.get_invisibles();
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
});
