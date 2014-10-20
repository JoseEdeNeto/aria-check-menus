(function () {
    window.InnerHTMLVerifier = (function () {
        return {
            saveState: function (representation, real_element) {
                representation.innerHTML = real_element.innerHTML;
            },
            checkChanges: function (representation, real_element) {
                if (representation.innerHTML !== real_element.innerHTML) {
                    var virtual_DOM = document.createElement(real_element.tagName),
                        virtual_childs = virtual_DOM.childNodes,
                        real_childs = real_element.childNodes;
                    virtual_DOM.innerHTML = representation.innerHTML;
                    if (real_childs.length !== virtual_childs.length)
                        return { target: real_element, type: "innerHTML" };
                }
                return null;
            }
        };
    }());
}());
