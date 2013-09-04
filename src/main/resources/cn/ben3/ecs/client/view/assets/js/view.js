App.gridView = Ember.View.extend({
    templateName: 'gridView',
    checked:true,
    records:[
        {HostName:"a",InstanceId:"b",Status:"run",InnerIpAddress:"100.1.1.1",PublicIpAddress:"1.1.1.1"},
        {HostName:"a",InstanceId:"b",Status:"run",InnerIpAddress:"100.1.1.1",PublicIpAddress:"1.1.1.1"},
        {HostName:"a",InstanceId:"b",Status:"run",InnerIpAddress:"100.1.1.1",PublicIpAddress:"1.1.1.1"},
        {HostName:"a",InstanceId:"b",Status:"run",InnerIpAddress:"100.1.1.1",PublicIpAddress:"1.1.1.1"},
        {HostName:"a",InstanceId:"b",Status:"run",InnerIpAddress:"100.1.1.1",PublicIpAddress:"1.1.1.1"},
        {HostName:"a",InstanceId:"b",Status:"run",InnerIpAddress:"100.1.1.1",PublicIpAddress:"1.1.1.1"}
    ],
    select:[],
    actions: {
        all:function(e){
            if(this.checked){
                this.set('checked',false);
            }else{
                this.set('checked', true);
            }
            this.rerender();
        },
        one: function(eve) {
            console.log(eve);
            this.rerender();
        }
    } ,

    checkOne:function(){

    },
    getChecked:function(){

    }

});