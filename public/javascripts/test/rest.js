'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

var testReq = function(){
   $.ajax({
        url: '/mock/rest/datetime',
        async: false,
        success: function(data) {
          console.debug(data);
        }
      });
 };

describe('MockServer', function() {

  describe('Rest module', function() {

    beforeEach(function() {
      browser().navigateTo('/');
    });

    it('should contain 1 request', function() {

      expect(repeater('.request').count()).toBe(0);

      setTimeout(testReq, 3000);

      sleep(4);

      expect(repeater('.request').count()).toBe(1);

      sleep(2);
      
    });
  });
});
