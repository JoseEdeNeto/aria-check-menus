(function () {
    window.InnerHTMLVerifier = (function () {
        return {
            saveState: function (representation, real_element) {
                representation.innerHTML = real_element.innerHTML;
            },
            checkChanges: function (representation, real_element) {
                if (representation.innerHTML !== real_element.innerHTML) {
                    return { target: real_element, type: "innerHTML" };
                }
                return null;
            }
        };
    }());
}());
