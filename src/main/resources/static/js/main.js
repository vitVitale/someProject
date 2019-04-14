//функция получение индекса объекта в массиве
function getIndex(list, id) {
    for(var i = 0; i < list.length; ++i) {
        if(list[i].id === id)
            return i;
    }
    return -1;
}

var messageApi = Vue.resource('/message{/id}');

Vue.component('messages-list', {
    props: {
        messages: Array
    },
    data: function() {
        return {
            //данные поля понадобятся нам при редактировании message
            message_id: 0,
            message_text: ''
        }
    },
    template:
        '<div style="position: relative; width: 300px;">' +
            '<div>' +
                '<input type="text" :placeholder="message_id" v-model="message_text"/>' +
                '<input type="button" value="Save" @click="save"/>' +
            '</div>' +
            '<div v-for="message in messages" :key="message.id">' +
                '<i>({{ message.id }})</i> {{ message.text }}' +
                '<span style="position: absolute; right: 0">' +
                    '<input type="button" value="Edit" @click="edit(message)"/>' +
                    '<input type="button" value="X" @click="del(message)" />' +
                '</span>' +
            '</div>' +
        '</div>',
    methods: {
        save: function() {
            //так как мы находимся в методе редактирования message, то необходимо брать его текущие поля,
            //а они у нас находятся в опции data данного компонента
            //если в элемент EditText не помещён message для редактирования, то поле message_id = 0
            if(this.message_id == 0) {
                //в методе save() передаём только поле message_text, т.к. поле id, будет изменено
                //на стороне сервера и в ответе мы уже получим полноценный message с утановленным id
                messageApi.save({}, { text: this.message_text }).then(result =>
                    result.json().then(data => {
                        this.messages.push(data);
                        this.message_id = 0;
                        this.message_text = '';
                    })
                );
            } else {
                //в параметре метода update() нам снова не нужен целый объект message, используем только поле
                // message_text, поле id будет установлено на сервере из первого параметра update()
                messageApi.update({ id: this.message_id }, { text: this.message_text }).then(result =>
                    result.json().then(data => {
                        //в data нам вернулся изменённый объект message, находим его индекс в массиве messages
                        var index = getIndex(this.messages, data.id);
                        //старый message удаляем из messages, а на его место вставляем новый объект с сервера
                        this.messages.splice(index, 1, data);
                        this.message_id = 0;
                        this.message_text = '';
                    })
                );
            }
        },
        edit: function(message) {
            //в методе edit мы просто заполняем поля опции data из переданного объекта message
            //поле message_text сразу будет отображено в элементе EditText
            this.message_id = message.id;
            this.message_text = message.text;
        },
        del: function(message) {
            //здесь нам нужен id из объекта message, а не из data,
            // в ответе сервера проверяем метод ok, должен быть равен true
            messageApi.remove({ id: message.id }).then(result => {
                if(result.ok) {
                    this.messages.splice(this.messages.indexOf(message), 1);
                    //также очищаем поля message_id и message_text,
                    // т.к. message может уже быть помещён в поле редактирования
                    this.message_id = 0;
                    this.message_text = '';
                }
            });
        }
    },
    //Здесь получили все записи с сервера и сохранили в массиве messages
    created: function() {
        messageApi.get().then(result =>
            result.json().then(data =>
                data.forEach(message => this.messages.push(message))
            )
        );
    }
});

var app = new Vue({
    el: '#app',
    template: '<messages-list :messages="messages" />',
    data: {
      messages: []
    }
});﻿