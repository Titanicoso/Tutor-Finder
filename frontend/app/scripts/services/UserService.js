'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.factory('UserService', UserService);
	UserService.$inject = ['$http'];
	
	function UserService($http) {
        var service = {};

        service.getAll = getAll;
        service.getById = getById;
        service.getByUsername = getByUsername;
        service.create = create;
        service.update = update;
        service.delete = delete_;

        return service;

        // dummy functions with no backend (real ones commented below)
        function getAll() {
            var deferred = $q.defer();
            deferred.resolve(getUsers());
            return deferred.promise;
        }

        function getById(id) {
            var deferred = $q.defer();
            var filtered = $filter('filter')(getUsers(), {id: id});
            var user = filtered.length ? filtered[0] : null;
            deferred.resolve(user);
            return deferred.promise;
        }

        function getByUsername(username) {
            var deferred = $q.defer();
            var filtered = $filter('filter')(getUsers(), {username: username});
            var user = filtered.length ? filtered[0] : null;
            deferred.resolve(user);
            return deferred.promise;
        }

        function create(user) {
            var deferred = $q.defer();

            // simulate api call with $timeout
            $timeout(function () {
                getByUsername(user.username)
                    .then(function (duplicateUser) {
                        if (duplicateUser !== null) {
                            deferred.resolve({success: false, message: 'Username "' + user.username + '" is already taken'});
                        } else {
                            var users = getUsers();

                            // assign id
                            var lastUser = users[users.length - 1] || {id: 0};
                            user.id = lastUser.id + 1;

                            // save to local storage
                            users.push(user);
                            setUsers(users);

                            deferred.resolve({success: true});
                        }
                    });
            }, 1000);

            return deferred.promise;
        }

        function update(user) {
            var deferred = $q.defer();

            var users = getUsers();
            for (var i = 0; i < users.length; i++) {
                if (users[i].id === user.id) {
                    users[i] = user;
                    break;
                }
            }
            setUsers(users);
            deferred.resolve();

            return deferred.promise;
        }

        function delete_(id) {
            var deferred = $q.defer();

            var users = getUsers();
            for (var i = 0; i < users.length; i++) {
                var user = users[i];
                if (user.id === id) {
                    users.splice(i, 1);
                    break;
                }
            }
            setUsers(users);
            deferred.resolve();

            return deferred.promise;
        }

        // private functions

        function getUsers() {
            if (!localStorage.users) {
                localStorage.users = JSON.stringify([]);
            }

            return JSON.parse(localStorage.users);
        }

        function setUsers(users) {
            localStorage.users = JSON.stringify(users);
        }

        /* function getAll() {
            return $http.get('/api/users').then(handleSuccess, handleError('Error getting all users'));
        }

        function getById(id) {
            return $http.get('/api/users/' + id).then(handleSuccess, handleError('Error getting user by id'));
        }

        function getByUsername(username) {
            return $http.get('/api/users/' + username).then(handleSuccess, handleError('Error getting user by username'));
        }

        function create(user) {
            return $http.post('/api/users', user).then(handleSuccess, handleError('Error creating user'));
        }

        function update(user) {
            return $http.put('/api/users/' + user.id, user).then(handleSuccess, handleError('Error updating user'));
        }

        function delete_(id) {
            return $http.delete('/api/users/' + id).then(handleSuccess, handleError('Error deleting user'));
        }

        // private functions

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            return function () {
                return { success: false, message: error };
            };
        } */
    };
});
