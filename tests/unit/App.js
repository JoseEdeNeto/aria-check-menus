require("blanket")({pattern: "aria-check-menus/lib/"});
var App = require("../../lib/App");

// promise stub
var Promise = function () {
    var self_args = arguments,
        self = this;
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

        it.skip("should find invisible elements in the list", function (done) {
            var driver_mock = {},
                app = App(driver_mock),
                expected_elements = [],
                result;
            driver_mock.findElements = function () {
                return expected_elements;
            };
            expected_elements[0] = {
                isDisplayed: function () {
                    return Promise(true);
                }
            };
            expected_elements[1] = {
                isDisplayed: function () {
                    return Promise(false);
                }
            };
            result = app.get_invisibles();
            result.should.have.length(1);
            result.should.containEql(expected_elements[1]);
            result.should.not.containEql(expected_elements[0]);
            done();
        });
    });
});
