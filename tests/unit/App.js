var App = require("../../lib/App");

describe("App", function () {

    describe("#get_invisible", function () {
        it("should find invisible elements", function (done) {
            var driver_mock = {},
                app = App(driver_mock),
                expected_elements = [],
                result;
            driver_mock.findElements = function () {
                arguments.length.should.be.equal(1);
                arguments[0].should.be.eql({css: "body *"});
                return expected_elements;
            };
            result = app.get_invisibles();
            result.should.have.length(0);
            result.should.be.equal(expected_elements);
            done();
        });

    });
});
