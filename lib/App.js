module.exports = function (driver) {
    var driver = driver;
    return {
        get_invisibles: function () {
            return driver.findElements({css: "body *"});
        }
    };
};
