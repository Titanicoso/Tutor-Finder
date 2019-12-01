'use strict';
define(['tutorFinder'], function(tutorFinder) {
    tutorFinder.service('toastService', ['$translate', '$mdToast', function($translate, $mdToast) {
		var toast;

        this.showAction = function(text) {

            if (this.toast) {
                this.toast.textContent($translate.instant(text));
            } else {
                this.toast = $mdToast.simple()
                    .textContent($translate.instant(text))
                    .actionKey('z')
                    .action($translate.instant('ERROR_OK'))
                    .highlightAction(true)
                    .highlightClass('md-accent')
                    .position('bottom left')
                    .hideDelay(0);
            }
            
            $mdToast.show(this.toast);
        };
    }]);
});
