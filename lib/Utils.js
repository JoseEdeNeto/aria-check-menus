(function () {
    window.Utils = (function () {
        var getNodeIndex,
            idsCount = 1,
            constants = { prefix: "aria-check-menus" };

        return {
            getSelector: function (target) {
                if (target.id)
                    return "#" + target.id;
                target.id = constants.prefix + idsCount++;
                return "#" + target.id;
            }
        };
    }());
}());
