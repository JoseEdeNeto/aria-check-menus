(function () {
    window.OverMutationRecorder = (function (verifiers) {
        var recorded_mutations = [],
            elements_representations = [],
            real_elements = document.getElementsByTagName("*"),
            all_elements = [],
            private_methods = {};

        for (var i = 0; i < real_elements.length; i++) {
            all_elements.push(real_elements[i]);
        };

        private_methods.save_state = function () {
            for (var i = 0; i < all_elements.length; i++) {
                elements_representations[i] = {};
                for (var j = 0; j < verifiers.length; j++) {
                    verifiers[j].saveState(elements_representations[i], all_elements[i]);
                };
            };
        };

        private_methods.track_changes = function () {
            for (var i = 0; i < all_elements.length; i++) {
                if (!elements_representations[i])
                    break;
                for (var j = 0; j < verifiers.length; j++) {
                    var mutation = verifiers[j].checkChanges(elements_representations[i], all_elements[i]);
                    if (mutation !== null)
                        recorded_mutations.push(mutation);
                };
            };
        };

        private_methods.save_state();

        return {
            popLastEvent: function () {
                private_methods.track_changes();
                return recorded_mutations.pop();
            }
        };
    });
}());
