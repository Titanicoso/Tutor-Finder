'use strict';
define(['tutorFinder', 'services/restApiService'], function(tutorFinder) {

    tutorFinder.service('courseFileService', ['restApiService', function(restService) {
        
        this.getCourseFiles = function(professorId, subjectId) {
            return restService.get('courses/' + professorId + '_' + subjectId + '/files', false);
        };

        this.convertBase64ToBlob = function(b64Data) {
            var sliceSize = 512;
            var byteCharacters = atob(b64Data);
            var byteArrays = [];
        
            for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
                var slice = byteCharacters.slice(offset, offset + sliceSize);
        
                var byteNumbers = new Array(slice.length);
                for (var i = 0; i < slice.length; i++) {
                    byteNumbers[i] = slice.charCodeAt(i);
                }
        
                var byteArray = new Uint8Array(byteNumbers);
        
                byteArrays.push(byteArray);
            }
        
            return new Blob(byteArrays);
          }

        this.downloadFile = function(file, data) {
            const a = document.createElement("a");
			document.body.appendChild(a);
			var blob = this.convertBase64ToBlob(data);
            var url = window.URL.createObjectURL(blob);
			a.href = url;
			a.download = file.name;
			a.click();
			a.remove();
        };

        this.getCourseFile = function(professorId, subjectId, id) {
            return restService.get('courses/' + professorId + '_' + subjectId + '/files/' + id, false);
        };

        this.upload = function(professorId, subjectId, description, file) {
            var data = new FormData();

            data.append('file', file);
            data.append('description', new Blob([description], {type: 'application/json'}));
            return restService.put('courses/' + professorId + '_' + subjectId + '/files', data, true);
        };

        this.delete = function(professorId, subjectId, id) {
            return restService.delete('courses/' + professorId + '_' + subjectId + '/files/' + id);
        };
    }]);

});
