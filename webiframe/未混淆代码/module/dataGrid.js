/**
 * 数据列表组件
 * date:2019-08-10   License By http://easyweb.vip
 */
layui.define(['laytpl', 'laypage'], function (exports) {
    var $ = layui.jquery;
    var laytpl = layui.laytpl;
    var laypage = layui.laypage;
    var mOnItemClick = {};
    var mOnToolBarClick = {};
    var dataGrid = {};

    /**
     * 渲染
     */
    dataGrid.render = function (param) {
        var defaultParam = {  // 默认参数
            page: false,
            loadMore: false,
            where: {},
            headers: {},
            method: 'GET',
            request: {
                pageName: 'page',
                limitName: 'limit'
            }
        };
        var defaultLoadMore = {  // 默认加载更多参数
            class: '',
            first: true,
            curr: 1,
            limit: 10,
            text: '加载更多',
            loadingText: '加载中...',
            noMoreText: '没有更多数据了~',
            errorText: '加载失败，请重试'
        };
        var defaultPage = {  // 默认分页参数
            class: '',
            limit: 10,
            layout: ['prev', 'page', 'next', 'skip', 'count', 'limit']
        };
        var options = $.extend(defaultParam, param);
        var elem = options.elem;  // 容器
        var templet = options.templet;  // 模板
        var done = options.done;  // 渲染完成回调
        var onItemClick = options.onItemClick;  // 列表点击事件
        var onToolBarClick = options.onToolBarClick;  // 工具按钮点击事件
        var $elem = $(elem);  // 当前容器
        var mDataList = [];  // 当前的数据
        if ('static' == $elem.css('position')) {
            $elem.css('position', 'relative');
        }

        initRender(options);
        bindEvent();  // 绑定事件

        /*
         * 初始化渲染
         */
        function initRender(option) {
            var data = option.data;  // 数据源
            var reqData = option.reqData;  // 异步加载的方法
            var page = option.page;  // 分页配置
            var loadMore = option.loadMore;   // 加载更多配置
            var parseData = option.parseData;  // 格式化返回数据
            var where = option.where;  // url方式的参数
            var headers = option.headers;  // url方式的header
            var method = option.method;  // url方式的请求方法
            var request = option.request;  // url方式格式化参数
            if (loadMore) {
                if (typeof loadMore == 'object') {
                    loadMore = $.extend(defaultLoadMore, loadMore);
                } else {
                    loadMore = defaultLoadMore;
                }
            }
            if (page) {
                if (typeof page == 'object') {
                    page = $.extend(defaultPage, page);
                } else {
                    page = defaultPage;
                }
            }
            $elem.html('');  // 重置容器
            $(elem + '+.ew-datagrid-loadmore').remove();
            $(elem + '+.ew-datagrid-page').remove();
            if (data) {
                if (typeof data == 'string') {  // url渲染模式
                    var reqDataTemp = function (param, callback) {
                        $elem.addClass('ew-datagrid-loading');
                        var where_t = dataGrid.deepClone(where);
                        where_t[request.pageName] = param.page;
                        where_t[request.limitName] = param.limit;
                        var ajaxParam = {
                            url: data,
                            data: where_t,
                            headers: headers,
                            type: method,
                            dataType: 'json',
                            success: function (result, status, xhr) {
                                $elem.removeClass('ew-datagrid-loading');
                                parseData && (result = parseData(result));
                                callback(result);
                            },
                            error: function (xhr) {
                                ajaxParam.success({code: xhr.status, msg: xhr.statusText});
                            }
                        };
                        $.ajax(ajaxParam);
                    };
                    doRender(reqDataTemp);
                } else {  // 数据渲染模式
                    if (loadMore) {  // 开启加载更多
                        renderLoadMore();
                        changeLoadMore(2);
                        renderData(data);
                        done && done(data, 1, data.length);
                    } else if (page) {  // 开启分页
                        page.count = data.length;
                        page.jump = function (obj, first) {
                            var start = (obj.curr - 1) * page.limit;
                            var end = start + page.limit;
                            (end > data.length) && (end = data.length);
                            var currData = [];
                            for (var i = start; i < end; i++) {
                                currData.push(data[i]);
                            }
                            renderData(currData, (obj.curr - 1) * obj.limit);
                            done && done(currData, obj.curr, obj.count);
                        };
                        renderPage();
                    } else {  // 不分页
                        renderData(data);
                        done && done(data, 1, data.length);
                    }
                }
            } else if (reqData) {  // 异步加载模式
                doRender(reqData);
            }

            /*
             * 非数据方式渲染
             */
            function doRender(reqDataTemp) {
                if (loadMore) {  // 开启加载更多
                    renderLoadMore();
                    // 点击事件
                    var $loadMore = $('#' + loadMore.elem);
                    $loadMore.click(function () {
                        if ($(this).hasClass('ew-loading')) {  // 已是加载中状态
                            return;
                        }
                        if (loadMore.first) {
                            loadMore.first = false;
                        } else {
                            loadMore.curr++;
                        }
                        changeLoadMore(1);  // 设置加载中状态
                        reqDataTemp({page: loadMore.curr, limit: loadMore.limit}, function (res) {
                            if (res.code == 0) {
                                if (res.data.length < loadMore.limit) {
                                    changeLoadMore(2);  // 没有更多数据
                                } else {
                                    changeLoadMore(0);  // 设置正常状态
                                }
                                if (loadMore.curr == 1) {
                                    $elem.html('');  // 第一页清空数据
                                    mDataList = [];
                                }
                                renderData(res.data, (loadMore.curr - 1) * loadMore.limit, true);
                                done && done(res.data, loadMore.curr, res.count);
                            } else {
                                changeLoadMore(3);  // 设置加载失败状态
                            }
                        });
                    });
                    $loadMore.trigger('click');
                } else if (page) {  // 开启分页
                    reqDataTemp({page: 1, limit: page.limit}, function (res) {
                        if (res.code == 0) {
                            page.count = res.count;
                            page.jump = function (obj, first) {
                                if (!first) {
                                    reqDataTemp({page: obj.curr, limit: obj.limit}, function (res) {
                                        if (res.code == 0) {
                                            renderData(res.data, (obj.curr - 1) * obj.limit);
                                            done && done(res.data, obj.curr, obj.count);
                                        } else {

                                        }
                                    });
                                }
                            };
                            renderPage();
                            renderData(res.data);
                        } else {

                        }
                    });
                } else {  // 不分页
                    reqDataTemp({page: 1}, function (res) {
                        if (res.code == 0) {
                            renderData(res.data);
                            done && done(res.data, 1, res.count);
                        } else {

                        }
                    });
                }
            }

            /*
             * 渲染分页组件
             */
            function renderPage() {
                page.elem = 'ew-datagrid-page-' + elem.substring(1);  // 生成id
                $elem.after('<div class="ew-datagrid-page ' + page.class + '" id="' + page.elem + '"></div>');
                laypage.render(page);
            }

            /*
             * 渲染加载更多组件
             */
            function renderLoadMore() {
                loadMore.elem = 'ew-datagrid-page-' + elem.substring(1);  // 生成id
                var loadMoreHtml = '<div id="' + loadMore.elem + '" class="ew-datagrid-loadmore ' + loadMore.class + '"><div><span class="ew-icon-loading"><i class="layui-icon layui-icon-loading-1 layui-anim layui-anim-rotate layui-anim-loop"></i></span><span class="ew-loadmore-text">' + loadMore.text + '</span></div></div>';
                $elem.after(loadMoreHtml);
            }

            /*
             * 更改loadMore状态
             * 0正常，1加载中，2没有更多数据，3加载失败
             */
            function changeLoadMore(state) {
                var $loadMore = $('#' + loadMore.elem);
                var $loadMoreText = $loadMore.find('.ew-loadmore-text');
                $loadMore.removeClass('ew-loading');
                if (state == 0) {
                    $loadMoreText.html(loadMore.text);
                } else if (state == 1) {
                    $loadMoreText.html(loadMore.loadingText);
                    $loadMore.addClass('ew-loading')
                } else if (state == 2) {
                    $loadMoreText.html(loadMore.noMoreText);
                } else {
                    $loadMoreText.html(loadMore.errorText);
                }
            }
        }

        /*
         * 渲染数据
         */
        function renderData(list, number, append) {
            (number == undefined) && (number = 0);
            (append == undefined) && (append = false);
            var rsHtml = '';
            for (var i = 0; i < list.length; i++) {
                var item = list[i];
                item.LAY_INDEX = i;
                item.LAY_NUMBER = i + number + 1;
                laytpl($(templet).html()).render(item, function (html) {
                    rsHtml += html;
                });
            }
            if (append) {
                for (var i = 0; i < list.length; i++) {
                    mDataList.push(list[i]);
                }
                $elem.append(rsHtml);
            } else {
                mDataList = list;
                $elem.html(rsHtml);
            }
            initChildren();
        }

        /*
         * 初始化所有item
         */
        function initChildren() {
            $elem.children().each(function (index) {
                var $this = $(this);
                $this.attr('data-index', index);
                $this.addClass('ew-datagrid-item');
            });
        }

        /*
         * 事件监听
         */
        function bindEvent() {
            // item点击事件
            $elem.off('click.dgItem').on('click.dgItem', '>.ew-datagrid-item', function (e) {
                var $item = $(this);
                var index = $item.data('index');
                onItemClick || (onItemClick = mOnItemClick[elem.substring(1)]);
                onItemClick && onItemClick(getObj(mDataList[index], index, e.currentTarget, undefined, e, $item));
            });
            // lay-event点击事件
            $elem.off('click.dgToolBar').on('click.dgToolBar', '[lay-event]', function (e) {
                e.stopPropagation();
                var $item = $(this).parentsUntil('.ew-datagrid-item').parent();
                var index = $item.data('index');
                var event = $(this).attr('lay-event');
                onToolBarClick || (onToolBarClick = mOnToolBarClick[elem.substring(1)]);
                onToolBarClick && onToolBarClick(getObj(mDataList[index], index, e.currentTarget, event, e, $item));
            });

            // 构建obj
            function getObj(data, index, elem, event, e, $item) {
                var obj = {data: data, index: index, elem: elem, event: event, e: e};
                // 提供删除的方法
                obj.del = function () {
                    $item.remove();
                    mDataList.splice(index, 1);
                    initChildren();
                };
                // 提供更新的方法
                obj.update = function (newObj) {
                    newObj.LAY_INDEX = mDataList[index].LAY_INDEX;
                    newObj.LAY_NUMBER = mDataList[index].LAY_NUMBER;
                    mDataList[index] = newObj;
                    laytpl($(templet).html()).render(newObj, function (html) {
                        $item.before(html);
                        $item.remove();
                        initChildren();
                    });
                };
                return obj;
            }
        }

        // 构建返回实例
        var _instance = {
            data: mDataList,
            reload: function (option) {
                initRender($.extend(options, option));
            }
        };
        return _instance;
    };

    /** 绑定事件 */
    dataGrid.onItemClick = function (id, callback) {
        mOnItemClick[id] = callback;
    };
    dataGrid.onToolBarClick = function (id, callback) {
        mOnToolBarClick[id] = callback;
    };

    /** 深度克隆对象 */
    dataGrid.deepClone = function (obj) {
        var result;
        var oClass = dataGrid.isClass(obj);
        if (oClass === "Object") {
            result = {};
        } else if (oClass === "Array") {
            result = [];
        } else {
            return obj;
        }
        for (var key in obj) {
            var copy = obj[key];
            if (dataGrid.isClass(copy) == "Object") {
                result[key] = arguments.callee(copy); // 递归调用
            } else if (dataGrid.isClass(copy) == "Array") {
                result[key] = arguments.callee(copy);
            } else {
                result[key] = obj[key];
            }
        }
        return result;
    };

    /** 获取变量类型 */
    dataGrid.isClass = function (o) {
        if (o === null)
            return "Null";
        if (o === undefined)
            return "Undefined";
        return Object.prototype.toString.call(o).slice(8, -1);
    };

    /** 自动渲染 */
    dataGrid.autoRender = function () {
        $('[data-grid]').each(function () {
            // 获取或补充容器id
            var $this = $(this);
            var elem = $this.attr('id');
            if (!elem) {
                elem = 'ew-datagrid-' + $('[id^="ew-datagrid-"]').length + 1;
                $this.attr('id', elem);
            }
            // 初始化模板
            var $tpl = $this.children('[data-grid-tpl]');
            if ($tpl.length > 0) {
                $tpl.attr('id', elem + '-tpl');
                $this.after($tpl[0].outerHTML);
                $tpl.remove();
                // 获取参数
                var options = $(this).attr('lay-data');
                options = parseAttrData(options);
                options.elem = '#' + elem;
                options.templet = '#' + elem + '-tpl';
                dataGrid.render(options);  // 渲染
            }
        });
    };
    dataGrid.autoRender();

    /** 解析lay-data数据 */
    function parseAttrData(data) {
        var rsData;
        if (data) {
            try {
                rsData = new Function('return ' + data)();
            } catch (e) {
                console.error('element property data- configuration item has a syntax error: ' + data);
            }
        }
        return rsData;
    }

    // css样式
    var commonCss = '';
    commonCss += '.ew-datagrid-loadmore, .ew-datagrid-page {';
    commonCss += '    text-align: center;';
    commonCss += '}';
    commonCss += '.ew-datagrid-loadmore {';
    commonCss += '    color: #666;';
    commonCss += '    cursor: pointer;';
    commonCss += '}';
    commonCss += '.ew-datagrid-loadmore > div {';
    commonCss += '    padding: 12px;';
    commonCss += '}';
    commonCss += '.ew-datagrid-loadmore > div:hover {';
    commonCss += '    background-color: rgba(0, 0, 0, .03);';
    commonCss += '}';
    commonCss += '.ew-datagrid-loadmore .ew-icon-loading {';
    commonCss += '    margin-right: 6px;';
    commonCss += '    display: none;';
    commonCss += '}';
    commonCss += '.ew-datagrid-loadmore.ew-loading .ew-icon-loading {';
    commonCss += '    display: inline;';
    commonCss += '}';
    commonCss += '.ew-datagrid-loading:before {';
    commonCss += '    content: "\e63d";';
    commonCss += '    font-family: layui-icon !important;';
    commonCss += '    font-size: 32px;';
    commonCss += '    color: #C3C3C3;';
    commonCss += '    position: absolute;';
    commonCss += '    left: 50%;';
    commonCss += '    margin-left: -16px;';
    commonCss += '    margin-top: -16px;';
    commonCss += '    z-index: 999;';
    commonCss += '    -webkit-animatione: layui-rotate 1s linear;';
    commonCss += '    animation: layui-rotate 1s linear;';
    commonCss += '    -webkit-animation-iteration-count: infinite;';
    commonCss += '    animation-iteration-count: infinite;';
    commonCss += '}';
    $('head').append('<style id="ew-css-datagrid">' + commonCss + '</style>');

    exports("dataGrid", dataGrid);
});