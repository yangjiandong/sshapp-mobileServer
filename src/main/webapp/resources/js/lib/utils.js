Ext.namespace('Divo');
Ext.namespace('Divo.Utils');
Ext.ns('Divo.util');
Divo.util.Tools = function() {
  var curUser = null, sysConfig = null, monthDays = null;
  var curDate = '', curTime = '';
  var bookCates = new Ext.util.MixedCollection();

  /** *************** private method ******************* */

  // 返回导入，采集，归集定义的状态
  // "data":{"valid_fail":"4","breaked":"9","succeed":"1","fail":"2","nothing":"0","doing":"3",
  // "run_succeed":"5"}
  function getStatuValues() {
    var result = {};
    Ext.Ajax.request({
          url : "common/getStatuValues",
          method : 'POST',
          scope : this,
          async : false,

          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              result = resp.data;
            }
          }
        });

    return result;
  }

  //
  function getSysCurStartEndDate() {
    var result = {};
    Ext.Ajax.request({
          url : "common/get_sys_cur_start_end_date",
          method : 'POST',
          scope : this,
          async : false,

          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              result = resp.data;
            }
          }
        });

    return result;
  }

  function getSysLastStartEndDate() {
    var result = {};
    Ext.Ajax.request({
          url : "common/get_sys_last_start_end_date",
          method : 'POST',
          scope : this,
          async : false,

          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              result = resp.data;
            }
          }
        });

    return result;
  }

  /**
   * 判断当前用户是否能维护基础字典的code和name属性
   */
  function checkUserEdit() {
    var val = true;

    Ext.Ajax.request({
          url : "common/checkUserEdit",
          scope : this,
          async : false,
          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              if (!resp.check)
                val = false;
            }
          }
        });

    return val;

  }

  /**
   * 判断是否为军队医院
   */
  function checkArmy() {
    var val = true;

    Ext.Ajax.request({
          url : "common/checkArmy",
          scope : this,
          async : false,
          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              if (!resp.check)
                val = false;
            }
          }
        });

    return val;

  }

  /**
   * 判断用户是否能操作工具栏,参数为权限
   */
  function hasPermission(name) {
    var val = true;

    Ext.Ajax.request({
          params : {
            "name" : name
          },
          url : "common/hasPermission",
          scope : this,
          async : false,
          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              if (!resp.check)
                val = false;
            }
          }
        });

    return val;
  }

  // 是否是核算员
  function isSetter() {
    var val = true;

    Ext.Ajax.request({
          url : "common/isSetter",
          scope : this,
          async : false,
          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              if (!resp.check)
                val = false;
            }
          }
        });

    return val;
  }

  /**
   * 验证用户是否进行核算单元授权
   */
  function checkIsAdmin() {
    var val = true;

    Ext.Ajax.request({
          url : "common/checkIsAdmin",
          scope : this,
          async : false,
          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              if (!resp.check)
                val = false;
            }
          }
        });

    return val;

  }

  function checkUserOrgs() {
    var val = true;

    Ext.Ajax.request({
          url : "common/checkUserOrgs",
          scope : this,
          async : false,
          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              if (!resp.check)
                val = false;
            }
          }
        });

    return val;

  }

  function getSysCurDate() {
    var result = '';
    Ext.Ajax.request({
          url : "common/get_sys_cur_date",
          method : 'POST',
          scope : this,
          async : false,

          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            result = resp.datetime;

          }
        });

    return result;
  }

  function checkIsCost(itemCode) {
    var result = false;
    Ext.Ajax.request({
          url : "reportItem/checkIsCost",
          params : {
            "itemCode" : itemCode
          },
          method : 'POST',
          scope : this,
          async : false,
          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            result = resp.flag;

          }
        });

    return result;
  };

  function getChartXml(url, params) {
    var result = {};
    Ext.Ajax.request({
          url : url,
          params : params,
          method : 'POST',
          scope : this,
          async : false,

          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            if (resp.success) {
              result = resp.xmldata;
            }
          }
        });

    return result;
  }

  // 判断是否按收入分摊
  function checkApportionModeCodeByIncome(code) {
    var result = false;
    Ext.Ajax.request({
          url : "apportionCostMode/checkApportionModeCodeByIncome",
          params : {
            "apportionModeCode" : code
          },
          method : 'POST',
          scope : this,
          async : false,
          callback : function(o, s, r) {
            var resp = Ext.decode(r.responseText);
            result = resp.flag;
          }
        });

    return result;
  };

  // 今日
  function getToday() {
    var today;
    var date = new Date();

    if (date.getMonth() < 9) {
      if (date.getDate() < 9) {
        today = date.getFullYear() + ".0" + (date.getMonth() + 1) + ".0"
            + date.getDate();
      } else {
        today = date.getFullYear() + ".0" + (date.getMonth() + 1) + "."
            + date.getDate();
      }
    } else {
      if (date.getDate() < 9) {
        today = date.getFullYear() + "." + (date.getMonth() + 1) + ".0"
            + date.getDate();
      } else {
        today = date.getFullYear() + "." + (date.getMonth() + 1) + "."
            + date.getDate();
      }
    }

    return today;
  }

  // 昨日
  function getYesterDay() {
    var yesterday;
    var date = new Date();
    var lastDay = date.valueOf() - (24 * 60 * 60 * 1000);

    date = new Date(lastDay);
    if (date.getMonth() < 9) {
      if (date.getDate() < 9) {
        yesterday = date.getFullYear() + ".0" + (date.getMonth() + 1) + ".0"
            + date.getDate();
      } else {
        yesterday = date.getFullYear() + ".0" + (date.getMonth() + 1) + "."
            + date.getDate();
      }
    } else {
      if (date.getDate() < 9) {
        yesterday = date.getFullYear() + "." + (date.getMonth() + 1) + ".0"
            + date.getDate();
      } else {
        yesterday = date.getFullYear() + "." + (date.getMonth() + 1) + "."
            + date.getDate();
      }
    }

    return yesterday;
  }

  // 本周第一日
  function getThisWeekFirstDate() {
    var thisWeekFirstDate;
    var today = new Date();
    var date = today.getFirstDateOfMonth();
    var n = today.getFirstDayOfMonth();
    if (n == 0)
      n = 7;

    date = date.add(Date.DAY, 8 - n);

    for (var i = 0; i < 5; i++) {
      date = date.add(Date.DAY, 7);

      if (date < today)
        thisWeekFirstDate = date;

    }

    date = thisWeekFirstDate;
    if (date.getMonth() < 9) {
      if (date.getDate() < 9) {
        thisWeekFirstDate = date.getFullYear() + ".0" + (date.getMonth() + 1)
            + ".0" + date.getDate();
      } else {
        thisWeekFirstDate = date.getFullYear() + ".0" + (date.getMonth() + 1)
            + "." + date.getDate();
      }
    } else {
      if (date.getDate() < 9) {
        thisWeekFirstDate = date.getFullYear() + "." + (date.getMonth() + 1)
            + ".0" + date.getDate();
      } else {
        thisWeekFirstDate = date.getFullYear() + "." + (date.getMonth() + 1)
            + "." + date.getDate();
      }
    }
    return thisWeekFirstDate;
  }

  // 本月第一日
  function getThisMonthFirstDate() {
    var thisMonthFirstDate;
    var date = new Date();

    if (date.getMonth() < 9) {
      thisMonthFirstDate = date.getFullYear() + ".0" + (date.getMonth() + 1)
          + ".01";
    } else {
      thisMonthFirstDate = date.getFullYear() + "." + (date.getMonth() + 1)
          + ".01";
    }

    return thisMonthFirstDate;
  }

  // 本季度第一日
  function getThisQuarterFirstDate() {
    var thisQuarterFirstDate;
    var date = new Date();
    var curMonth = date.getMonth();

    if (curMonth < 3) {
      thisQuarterFirstDate = date.getFullYear() + ".01.01";
    } else if (curMonth < 6) {
      thisQuarterFirstDate = date.getFullYear() + ".04.01";
    } else if (curMonth < 9) {
      thisQuarterFirstDate = date.getFullYear() + ".07.01";
    } else {
      thisQuarterFirstDate = date.getFullYear() + ".10.01";
    }

    return thisQuarterFirstDate;
  }

  // 本年度第一日
  function getThisYearFirstDate() {
    var thisYearFirstDate;
    var date = new Date();

    thisYearFirstDate = date.getFullYear() + ".01.01";

    return thisYearFirstDate;
  }
  /** *********** public method ************ */
  return {
    /**
     * 初始化工具类函数
     */
    init : function() {
      // getClientCurUser();
      // getSrvCurrentDate();
      // getSystemConfig();
      // getSrvCurMonthDays();
      // getBookCateInfos();
    },
    /**
     * 获取当前用户信息
     */
    getCurUser : function() {
      return curUser;
    },
    /**
     * 获取字符串的助记符
     */
    getMemo : function(str) {
      return getMemo(str);
    },
    /**
     * 获取服务器端的当前日期
     */
    getServerNowDate : function() {
      return curDate;
    },
    /**
     * 获取服务器端的当前时间
     */
    getServerNowTime : function() {
      getSrvCurrentTime();
      return curTime;
    },
    /**
     * 获取系统配置信息
     */
    getSysConfig : function() {
      return sysConfig;
    },
    /**
     * 根据当前字符串,获取五笔助记符和拼音助记符
     */
    getStrMemo : function(str) {
      return getMenmo(str);
    },

    // 返回导入，采集，归集定义的状态
    getStatuValues : function() {
      return getStatuValues();
    },

    //
    getSysCurStartEndDate : function() {
      return getSysCurStartEndDate();
    },

    //
    getSysLastStartEndDate : function() {
      return getSysLastStartEndDate();
    },

    getSysLastStartEndDateByDate : function(date) {

      var result = {};
      Ext.Ajax.request({
            url : "common/get_sys_last_start_end_date_by_date",
            params : {
              "date" : date
            },
            method : 'POST',
            scope : this,
            async : false,

            callback : function(o, s, r) {
              var resp = Ext.decode(r.responseText);
              if (resp.success) {
                result = resp.data;
              }
            }
          });

      return result;

    },

    getSysPrevStartEndDateByDate : function(date) {

      var result = {};
      Ext.Ajax.request({
            url : "common/get_sys_prev_start_end_date_by_date",
            params : {
              "date" : date
            },
            method : 'POST',
            scope : this,
            async : false,

            callback : function(o, s, r) {
              var resp = Ext.decode(r.responseText);
              if (resp.success) {
                result = resp.data;
              }
            }
          });

      return result;

    },

    checkUserEdit : function() {
      return checkUserEdit();
    },

    checkArmy : function() {
      return checkArmy();
    },

    hasPermission : function(name) {
      return hasPermission(name);
    },

    isSetter : function() {
      return isSetter(name);
    },

    checkUserOrgs : function() {
      return checkUserOrgs();
    },
    checkIsAdmin : function() {
      return checkIsAdmin();
    },
    getSysCurDate : function() {
      return getSysCurDate();
    },
    checkIsCost : function(itemCode) {
      return checkIsCost(itemCode);
    },

    getChartXml : function(url, p) {
      return getChartXml(url, p);
    },
    checkApportionModeCodeByIncome : function(code) {
      return checkApportionModeCodeByIncome(code);
    },
    getToday : function() {
      return getToday();
    },
    getYesterDay : function() {
      return getYesterDay();
    },
    getThisWeekFirstDate : function() {
      return getThisWeekFirstDate();
    },
    getThisMonthFirstDate : function() {
      return getThisMonthFirstDate();
    },
    getThisQuarterFirstDate : function() {
      return getThisQuarterFirstDate();
    },
    getThisYearFirstDate : function() {
      return getThisYearFirstDate();
    }

  }
}();

Divo.util.Tools.init();

// 统一调用startDate,endDate设置

function getPrevDateByDate() {
  var d = Ext.getCmp('startDateField').getRawValue();
  var s = Divo.util.Tools.getSysPrevStartEndDateByDate(d);
  Ext.getCmp('startDateField').setValue(s.startDate);
  Ext.getCmp('endDateField').setValue(s.endDate);
}

function getLastDateByDate() {
  var d = Ext.getCmp('startDateField').getRawValue();
  var s = Divo.util.Tools.getSysLastStartEndDateByDate(d);
  Ext.getCmp('startDateField').setValue(s.startDate);
  Ext.getCmp('endDateField').setValue(s.endDate);
}
