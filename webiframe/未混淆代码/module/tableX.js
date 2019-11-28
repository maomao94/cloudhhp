/**
 * 表格扩展模块
 * date:2019-07-12   License By http://easyweb.vip
 */
layui.define(['layer', 'table', 'laytpl', 'form', 'util', 'contextMenu'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var table = layui.table;
    var laytpl = layui.laytpl;
    var form = layui.form;
    var util = layui.util;
    var contextMenu = layui.contextMenu;
    var device = layui.device;
    var tbSearchAttr = 'tb-search';  // 前端搜索属性
    var tbRefreshAttr = 'tb-refresh';  // 刷新按钮属性
    var tbExportAttr = 'tb-export';  // 导出按钮属性
    var txFieldPre = 'txField_';  // templte列filed前缀

    var tableX = {
        // 合并相同单元格
        merges: function (tableId, indexs, fields, sort) {
            // 检查参数是否为空
            if (!tableId) {
                console.error('table filter not be null');
                return;
            }
            if (!indexs) {
                console.warn('merge indexs not be null');
                return;
            }
            if (typeof fields === 'boolean') {
                sort = fields;
                fields = undefined;
            }
            var $tb = $('[lay-filter="' + tableId + '"]+.layui-table-view>.layui-table-box>.layui-table-body>table');
            var $trs = $tb.find('>tbody>tr');
            $tb.addClass('layui-table-x');

            // 合并一列
            function merge(tableId, index, field) {
                var data = table.cache[tableId];
                if (data.length > 0) {
                    var lastValue, spanNum = 1;
                    if (field) {
                        lastValue = data[0][field];
                    } else {
                        lastValue = $trs.eq(0).find('td').eq(index).find('.layui-table-cell').html();
                    }
                    for (var i = 1; i < data.length; i++) {
                        var currentValue;
                        if (field) {
                            currentValue = data[i][field];
                        } else {
                            currentValue = $trs.eq(i).find('td').eq(index).find('.layui-table-cell').html();
                        }
                        if (currentValue == lastValue) {
                            spanNum++;
                            if (i == data.length - 1) {
                                $trs.eq(i - spanNum + 1).find('td').eq(index).attr('rowspan', spanNum);
                                for (var j = 1; j < spanNum; j++) {
                                    $trs.eq(i - j + 1).find('td').eq(index).attr('del', 'true');
                                }
                            }
                        } else {
                            $trs.eq(i - spanNum).find('td').eq(index).attr('rowspan', spanNum);
                            for (var j = 1; j < spanNum; j++) {
                                $trs.eq(i - j).find('td').eq(index).attr('del', 'true');
                            }
                            spanNum = 1;
                            lastValue = currentValue;
                        }
                    }
                }
            }

            // 循环合并每一列
            for (var i = 0; i < indexs.length; i++) {
                if (!fields) {
                    merge(tableId, indexs[i], undefined);
                } else {
                    merge(tableId, indexs[i], fields[i]);
                }
            }
            $trs.find('[del="true"]').remove();  // 移除多余的单元格
            // 监听排序事件
            (sort == undefined) && (sort = true);
            if (sort) {
                table.on('sort(' + tableId + ')', function (obj) {
                    tableX.merges(tableId, indexs, fields);
                });
            }
        },
        // 导出表格数据
        exportData: function (param) {
            var cols = param.cols;  // 表头
            var dataList = param.data;  // 数据
            var fileName = param.fileName;  // 文件名
            var fileType = param.expType;  // 类型，xls、csv
            var option = param.option;  // url方式加载的配置
            option || (option = {});

            if (device.ie) return layer.msg('不支持ie导出');

            // data为url先请求数据
            if (dataList && typeof dataList === 'string') {
                var loadIndex = layer.msg('加载中..', {icon: 16, shade: 0.01, time: 0});
                option.url = dataList;
                tableX.loadUrl(option, function (res) {
                    layer.close(loadIndex);
                    param.data = res;
                    tableX.exportData(param);
                });
                return;
            }
            // 列参数补全
            for (var i = 0; i < cols.length; i++) {
                for (var j = 0; j < cols[i].length; j++) {
                    if (cols[i][j].type == undefined) {
                        cols[i][j].type = 'normal';
                    }
                    if (cols[i][j].hide == undefined) {
                        cols[i][j].hide = false;
                    }
                }
            }
            var titles = [], fields = [], expData = [];
            // 获取表头和表头字段名
            table.eachCols(undefined, function (i, item) {
                if (item.type == 'normal' && !item.hide) {
                    titles.push(item.title ? item.title : '');
                    fields.push(item.field ? item.field : (txFieldPre + i));
                }
            }, cols);
            // 格式化数据
            var allList = tableX.parseTbData(cols, tableX.deepClone(dataList), true);
            for (var i = 0; i < allList.length; i++) {
                var rowItem = [];
                for (var j = 0; j < fields.length; j++) {
                    var itemVal = allList[i][fields[j]];
                    if (typeof itemVal == 'string') {
                        itemVal = itemVal.replace(/,/g, '，');
                    }
                    rowItem.push(itemVal);
                }
                expData.push(rowItem);
            }
            // 创建下载文件的a标签
            var textType = ({csv: 'text/csv', xls: 'application/vnd.ms-excel'})[fileType];
            var alink = document.createElement("a");
            var getDataStr = function () {
                var dataTitle = [], dataMain = [];
                layui.each(expData, function (i1, item1) {
                    var vals = [];
                    layui.each(titles, function (i, item) {
                        i1 == 0 && dataTitle.push(item || '');
                    });
                    layui.each(table.clearCacheKey(item1), function (i2, item2) {
                        vals.push(item2);
                    });
                    dataMain.push(vals.join(','))
                });
                return titles.join(',') + '\r\n' + expData.join('\r\n');
            };
            alink.href = 'data:' + textType + ';charset=utf-8,\ufeff' + encodeURIComponent(getDataStr());
            alink.download = (fileName || 'table') + '.' + (fileType ? fileType : 'xls');
            document.body.appendChild(alink);
            alink.click();
            document.body.removeChild(alink);
        },
        // 后端导出数据拼接参数
        exportDataBack: function (url, where, method) {
            where || (where = {});
            if (!method || method.toString().toLowerCase() == 'get') {
                var param = '';
                for (var f in where) {
                    if (!param) {
                        param = '?' + f + '=' + where[f];
                    } else {
                        param += ('&' + f + '=' + where[f]);
                    }
                }
                window.open(url + param);
            } else {
                var htmlStr = '<html><body style="display: none;"><form id="eFrom" action="' + url + '" method="' + method + '">';
                for (var f in where) {
                    htmlStr += ('<textarea name="' + f + '">' + where[f] + '</textarea>');
                }
                htmlStr += '</form></body></html>';
                $('#exportFrame').remove();
                $('body').append('<iframe id="exportFrame" style="display: none;"></iframe>');
                var eFrame = document.getElementById('exportFrame');
                var eWindow = eFrame.contentWindow;
                var eDocument = eWindow.document;
                eWindow.focus();
                eDocument.open();
                eDocument.write(htmlStr);
                eDocument.close();
                eDocument.getElementById("eFrom").submit();
            }
        },
        // 渲染表格，后端排序
        render: function (param) {
            var tableId = $(param.elem).attr('lay-filter');
            param.autoSort = false;  // 关闭默认排序
            var insTb = table.render(param);  // 渲染表格
            // 排序监听
            table.on('sort(' + tableId + ')', function (obj) {
                var sortField = obj.field, sortType = obj.type; // 排序字段、类型
                var sortWhere = $.extend(param.where, {sort: sortField, order: sortType});
                insTb.reload({where: sortWhere, page: {curr: 1}});
            });
            return insTb;
        },
        // 渲染表格，前端分页
        renderFront: function (param) {
            var insTb, tableId = $(param.elem).attr('lay-filter');
            param.autoSort = false;  // 关闭默认排序
            // 没有field的templet列补上，因为排序必须有filed字段，否则点击排序会报错
            for (var i = 0; i < param.cols.length; i++) {
                for (var j = 0; j < param.cols[i].length; j++) {
                    if (param.cols[i][j].templet && !param.cols[i][j].field) {
                        param.cols[i][j].field = txFieldPre + i + '_' + j;
                    }
                }
            }
            if (param.url) {  // url方式
                var xParam = tableX.deepClone(param);
                xParam.data = [], xParam.url = '';
                insTb = table.render(xParam);  // 先渲染表格结构
                // 提供刷新方法
                insTb.reloadUrl = function (p) {
                    var reParam = tableX.deepClone(param);
                    p && (reParam = $.extend(reParam, p));
                    var loadIndex = layer.msg('加载中..', {icon: 16, shade: 0.01, time: 0});
                    tableX.loadUrl(reParam, function (data) {  // 获取url数据
                        layer.close(loadIndex);
                        tableX.parseTbData(reParam.cols, data);  // 解析temple列
                        tableX.putTbData(tableId, data);  // 缓存数据
                        $('input[' + tbSearchAttr + '="' + tableId + '"]').val('');  // 清空搜索输入框
                        window.tbX.cacheSearch[tableId] = undefined;  // 重置搜索结果
                        insTb.reload({url: '', data: data, page: {curr: 1}});
                    });
                };
                // 请求数据
                var loadIndex = layer.msg('加载中..', {icon: 16, shade: 0.01, time: 0});
                tableX.loadUrl(param, function (data) {  // 获取url数据
                    layer.close(loadIndex);
                    tableX.parseTbData(param.cols, data);  // 解析temple列
                    tableX.putTbData(tableId, data);  // 缓存数据
                    insTb.reload({url: '', data: data, page: {curr: 1}});
                });
                tableX.renderAllTool(insTb);  // 渲染工具组件
            } else {
                tableX.parseTbData(param.cols, param.data);  // 解析temple列
                tableX.putTbData(tableId, param.data);  // 缓存数据
                insTb = table.render(param);  // 渲染表格
                tableX.renderAllTool(insTb);  // 渲染工具组件
                // 提供刷新的方法
                insTb.reloadData = function (p) {
                    insTb.reload(p);
                    tableX.parseTbData(param.cols, p.data);  // 解析temple列
                    tableX.putTbData(tableId, p.data);
                    $('input[' + tbSearchAttr + '="' + tableId + '"]').val('');  // 清空搜索输入框
                    window.tbX.cacheSearch[tableId] = undefined;  // 重置搜索结果
                };
            }
            return insTb;
        },
        // 渲染所有组件
        renderAllTool: function (insTb) {
            renderRefresh(insTb);  // 刷新
            renderFrontSort(insTb);  // 排序
            renderFrontSearch(insTb);  // 搜索
            renderExport(insTb);  // 导出
        },
        // 加载表格数据
        loadUrl: function (options, callback) {
            // 响应数据的自定义格式
            options.response = $.extend({
                statusName: 'code',
                statusCode: 0,
                msgName: 'msg',
                dataName: 'data',
                countName: 'count'
            }, options.response);
            var request = options.request, response = options.response, params = {};
            var data = $.extend(params, options.where);   // 参数
            if (options.contentType && options.contentType.indexOf("application/json") == 0) {
                data = JSON.stringify(data);  // 提交 json 格式
            }
            $.ajax({
                type: options.method || 'get',
                url: options.url,
                contentType: options.contentType,
                data: data,
                dataType: 'json',
                headers: options.headers || {},
                success: function (res) {
                    // 如果有数据解析的回调，则获得其返回的数据
                    if (typeof options.parseData === 'function') {
                        res = options.parseData(res) || res;
                    }
                    // 检查数据格式是否符合规范
                    if (res[response.statusName] != response.statusCode) {
                        var msgText = res[response.msgName] || ('返回的数据不符合规范，正确的成功状态码 (' + response.statusName + ') 应为：' + response.statusCode);
                        layer.msg(msgText, {icon: 2});
                    } else {
                        callback(res[response.dataName]);
                    }
                },
                error: function (e, m) {
                    layer.msg('数据接口请求异常：' + m, {icon: 2});
                }
            });
        },
        // 解析数据表格templet列
        parseTbData: function (cols, dataList, overwrite) {
            var templets = [];  // 需要解析的列
            table.eachCols(undefined, function (i, item) {
                if (item.templet) {
                    var one = {field: ((item.field && (overwrite || item.field.indexOf(txFieldPre) == 0)) ? item.field : ('txField_' + i))};
                    if (typeof item.templet === 'string') {
                        one.templet = function (d) {  // templet列使用laytpl渲染
                            var rsStr = undefined;
                            laytpl($(item.templet).html()).render(d, function (html) {
                                rsStr = html;
                            });
                            return rsStr;
                        }
                    } else {
                        one.templet = item.templet;
                    }
                    templets.push(one);
                }
            }, cols);
            for (var i = 0; i < dataList.length; i++) {
                var current = dataList[i];
                for (var j = 0; j < templets.length; j++) {
                    var htmlStr = '<div>' + templets[j].templet(current) + '</div>';
                    current[templets[j].field] = $(htmlStr).not('.export-hide').text().replace(/(^\s*)|(\s*$)/g, '');  // 去除前后空格
                }
            }
            return dataList;
        },
        // 获取表格缓存的数据
        getTbData: function (tableId) {
            var dataList = window.tbX.cache[tableId];
            if (dataList == undefined) {
                dataList = table.cache[tableId];
            }
            return tableX.deepClone(dataList);
        },
        // 缓存表格的数据
        putTbData: function (tableId, dataList) {
            window.tbX.cache[tableId] = dataList;
            return dataList;
        },
        // 行绑定鼠标右键
        bindCtxMenu: function (tableId, items) {
            var dataList = table.cache[tableId];
            var tempItems = [];
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                tempItems.push({
                    icon: item.icon,
                    name: item.name,
                    click: function (e, event) {
                        var layId = $(e.currentTarget).attr('lay-id');
                        var $tr = $(event.currentTarget);
                        var trIndex = $tr.attr('data-index');
                        items[parseInt(layId.substring(8))].click(dataList[trIndex]);
                        $tr.removeClass('layui-table-click');
                    }
                });
            }
            var elem = '#' + tableId + '+.layui-table-view .layui-table-body tr';
            $(elem).bind('contextmenu', function (e) {
                $(elem).removeClass('layui-table-click');
                $(this).addClass('layui-table-click');
                contextMenu.show(tempItems, e.clientX, e.clientY, e);
                return false;
            });
        },
        // 搜索数据
        filterData: function (dataList, searchName, searchValue) {
            var newList = [], sfs;
            for (var i = 0; i < dataList.length; i++) {
                var obj = dataList[i];
                if (!sfs) {  // 搜索的字段
                    if (!searchName) {
                        sfs = [];
                        for (var f in obj) {
                            sfs.push(f);
                        }
                    } else {
                        sfs = searchName.split(',');
                    }
                }
                for (var j = 0; j < sfs.length; j++) {
                    if (tableX.isContains(obj[sfs[j]], searchValue)) {
                        newList.push(obj);
                        break;
                    }
                }
            }
            return newList;
        },
        // 字符串是否包含
        isContains: function (str1, str2) {
            str1 || (str1 = '');
            str2 || (str2 = '');
            str1 = str1.toString().toLowerCase();
            str2 = str2.toString().toLowerCase();
            if (str1 == str2 || str1.indexOf(str2) != -1) {
                return true;
            }
            return false;
        },
        // 深度克隆对象
        deepClone: function (obj) {
            var result;
            var oClass = tableX.isClass(obj);
            if (oClass === "Object") {
                result = {};
            } else if (oClass === "Array") {
                result = [];
            } else {
                return obj;
            }
            for (var key in obj) {
                var copy = obj[key];
                if (tableX.isClass(copy) == "Object") {
                    result[key] = arguments.callee(copy);//递归调用
                } else if (tableX.isClass(copy) == "Array") {
                    result[key] = arguments.callee(copy);
                } else {
                    result[key] = obj[key];
                }
            }
            return result;
        },
        // 获取变量类型
        isClass: function (o) {
            if (o === null)
                return "Null";
            if (o === undefined)
                return "Undefined";
            return Object.prototype.toString.call(o).slice(8, -1);
        }
    };

    // 创建数据缓存对象
    window.tbX || (window.tbX = new Object());
    window.tbX.cache || (window.tbX.cache = new Object());
    window.tbX.cacheSearch || (window.tbX.cacheSearch = new Object());

    // 前端搜索
    var renderFrontSearch = function (insTb) {
        var tableId = insTb.config.id, $input = $('input[' + tbSearchAttr + '="' + tableId + '"]');
        if (!($input && $input.length > 0)) {
            return;
        }
        if (!$input.attr('placeholder')) {
            $input.attr('placeholder', '输入关键字按回车键搜索');
        }
        $input.off('keydown').on('keydown', function (event) {
            if (event.keyCode != 13) {
                return;
            }
            var searchName = $input.attr('name');  // 搜索的字段名，用逗号分隔
            var searchValue = $input.val().replace(/(^\s*)|(\s*$)/g, '');  // 搜索关键字
            var loadIndex = layer.msg('搜索中..', {icon: 16, shade: 0.01, time: 0});
            var dataList = tableX.getTbData(tableId);
            var newDataList = tableX.filterData(dataList, searchName, searchValue);
            window.tbX.cacheSearch[tableId] = newDataList;  // 缓存搜索后的数据用于排序
            insTb.reload({url: '', data: newDataList, page: {curr: 1}});
            layer.close(loadIndex);
        });
    };

    // 前端排序
    var renderFrontSort = function (insTb) {
        var tableId = insTb.config.id;
        table.on('sort(' + tableId + ')', function (obj) {
            var sortField = obj.field, sortType = obj.type; // 排序字段、类型
            var loadIndex = layer.msg('加载中..', {icon: 16, shade: 0.01, time: 0});
            var dataList = window.tbX.cacheSearch[tableId];  // 表格搜索后的数据
            dataList || (dataList = tableX.getTbData(tableId));
            if (sortType) {
                dataList = dataList.sort(function (o1, o2) {
                    var o1Str = o1[sortField], o2Str = o2[sortField];
                    if (sortType == 'asc') {  // 升序
                        return (o1Str == o2Str) ? 0 : ((o1Str < o2Str) ? -1 : 1);
                    } else {  // 降序
                        return (o1Str == o2Str) ? 0 : ((o1Str < o2Str) ? 1 : -1);
                    }
                });
            }
            insTb.reload({initSort: obj, url: '', data: dataList, page: {curr: 1}});
            layer.close(loadIndex);
        });
    };

    // 表格刷新按鈕
    var renderRefresh = function (insTb) {
        $('[' + tbRefreshAttr + '="' + insTb.config.id + '"]').off('click').on('click', function () {
            if (insTb.reloadUrl) {
                insTb.reloadUrl();
            } else {
                insTb.reload({page: {curr: 1}});
            }
        });
    };

    // 渲染导出按钮
    var renderExport = function (insTb) {
        var tableId = insTb.config.id;
        $('[' + tbExportAttr + '="' + tableId + '"]').off('click').on('click', function (event) {
            if ($(this).find('.tbx-dropdown-menu').length > 0) {
                return;
            }
            if (event !== void 0) {
                event.preventDefault();
                event.stopPropagation();
            }
            var htmlStr = '<div class="tbx-dropdown-menu">';
            htmlStr += '      <div class="tbx-dropdown-menu-item" data-type="check">导出选中数据</div>';
            htmlStr += '      <div class="tbx-dropdown-menu-item" data-type="current">导出当前页数据</div>';
            htmlStr += '      <div class="tbx-dropdown-menu-item" data-type="all">导出全部数据</div>';
            htmlStr += '   </div>';
            $(this).append(htmlStr);
            $(this).addClass('tbx-dropdown-btn');
            $(this).parent().css('position', 'relative');
            $(this).parent().css('z-index', '9998');
            $('.tbx-dropdown-menu').off('click').on('click', '.tbx-dropdown-menu-item', function (event) {
                var type = $(this).data('type');
                if (type == 'check') {
                    var checkRows = table.checkStatus(tableId);
                    if (checkRows.data.length == 0) {
                        layer.msg('请选择要导出的数据', {icon: 2});
                    } else {
                        $('.tbx-dropdown-menu').remove();
                        tableX.exportData({
                            fileName: insTb.config.title,
                            cols: insTb.config.cols,
                            data: checkRows.data
                        });
                    }
                } else if (type == 'current') {
                    tableX.exportData({
                        fileName: insTb.config.title,
                        cols: insTb.config.cols,
                        data: table.cache[tableId]
                    });
                } else if (type == 'all') {
                    tableX.exportData({
                        fileName: insTb.config.title,
                        cols: insTb.config.cols,
                        data: tableX.getTbData(tableId)
                    });
                }
                if (event !== void 0) {
                    event.preventDefault();
                    event.stopPropagation();
                }
            });
        });
        $(document).off('click.tbxDropHide').on('click.tbxDropHide', function () {
            $('.tbx-dropdown-menu').remove();
        });
    };

    // css样式
    var getCommonCss = function () {
        // 下拉菜单样式
        var cssStr = '.tbx-dropdown-btn {';
        cssStr += '        position: relative;';
        cssStr += '   }';
        cssStr += '   .tbx-dropdown-btn:hover {';
        cssStr += '        opacity: 1';
        cssStr += '   }';
        cssStr += '   .tbx-dropdown-menu {';
        cssStr += '        position: absolute;';
        cssStr += '        top: 100%;';
        cssStr += '        right: 0;';
        cssStr += '        padding: 5px 0;';
        cssStr += '        margin: 5px 0 0 0;';
        cssStr += '        overflow: visible;';
        cssStr += '        min-width: 110px;';
        cssStr += '        background: #fff;';
        cssStr += '        border-radius: 2px;';
        cssStr += '        box-shadow: 0 2px 4px rgba(0, 0, 0, .12);';
        cssStr += '        border: 1px solid #d2d2d2;';
        cssStr += '        z-index: 9998;';
        cssStr += '        cursor: default;';
        cssStr += '   }';
        cssStr += '   .tbx-dropdown-menu .tbx-dropdown-menu-item {';
        cssStr += '        display: block;';
        cssStr += '        color: #555;';
        cssStr += '        font-size: 14px;';
        cssStr += '        padding: 10px 15px;';
        cssStr += '        text-decoration: none;';
        cssStr += '        white-space: nowrap;';
        cssStr += '        cursor: pointer;';
        cssStr += '        user-select: none;';
        cssStr += '        line-height: normal;';
        cssStr += '   }';
        cssStr += '   .tbx-dropdown-menu .tbx-dropdown-menu-item:hover {';
        cssStr += '        background-color: #eeeeee;';
        cssStr += '   }';
        cssStr += '   .export-show {';
        cssStr += '        display: none;';
        cssStr += '   }';
        return cssStr;
    };

    $('head').append('<style>' + getCommonCss() + '</style>');
    exports("tableX", tableX);
});