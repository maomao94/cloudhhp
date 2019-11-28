/** EasyWeb iframe v3.1.5 date:2019-10-05 License By http://easyweb.vip */

layui.define(['layer', 'element', 'admin'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var element = layui.element;
    var admin = layui.admin;
    var headerDOM = '.layui-layout-admin>.layui-header';
    var sideDOM = '.layui-layout-admin>.layui-side>.layui-side-scroll';
    var bodyDOM = '.layui-layout-admin>.layui-body';
    var tabDOM = bodyDOM + '>.layui-tab';
    var titleDOM = bodyDOM + '>.layui-body-header';
    var tabFilter = 'admin-pagetabs';
    var navFilter = 'admin-side-nav';
    var tabEndCall = {};  // Tab关闭的事件回调
    var mIsAddTab = false;  // 是否是添加Tab，添加Tab的时候切换不自动刷新
    var homeUrl;  // 主页地址，主页不参与Tab记忆

    var index = {
        pageTabs: true,  // 是否开启多标签
        cacheTab: true,  // 是否记忆打开的选项卡
        openTabCtxMenu: true,  // 是否开启Tab右键菜单
        maxTabNum: 20,  // 最多打开多少个tab
        mTabList: [], // 当前打开的Tab
        mTabPosition: undefined, // 当前选中的Tab
        /* 加载主体部分 */
        loadView: function (param) {
            var menuPath = param.menuPath;
            var menuName = param.menuName;

            if (!menuPath) {
                console.error('url不能为空');
                layer.msg('url不能为空', {icon: 2});
                return;
            }
            // 是否开启多标签
            if (index.pageTabs) {
                // 判断选项卡是否已添加
                var flag = false;
                $(tabDOM + '>.layui-tab-title>li').each(function () {
                    if ($(this).attr('lay-id') === menuPath) {
                        flag = true;
                        return false;
                    }
                });
                // 没有则添加
                if (!flag) {
                    if ((index.mTabList.length + 1) >= index.maxTabNum) {
                        layer.msg('最多打开' + index.maxTabNum + '个选项卡', {icon: 2});
                        admin.activeNav(index.mTabPosition);
                        return;
                    }
                    mIsAddTab = true;
                    element.tabAdd(tabFilter, {
                        id: menuPath,
                        title: '<span class="title">' + (menuName ? menuName : '') + '</span>',
                        content: '<iframe lay-id="' + menuPath + '" src="' + menuPath + '" frameborder="0" class="admin-iframe"></iframe>'
                    });
                    // 记忆选项卡
                    if (menuPath != homeUrl) {
                        index.mTabList.push(param);
                    }
                    if (index.cacheTab) {
                        admin.putTempData('indexTabs', index.mTabList);
                    }
                }
                // 切换到该选项卡
                element.tabChange(tabFilter, menuPath);
            } else {
                var $contentDom = $(bodyDOM + '>.admin-iframe');
                if (!$contentDom || $contentDom.length <= 0) {
                    var contentHtml = '<div class="layui-body-header">';
                    contentHtml += '      <span class="layui-body-header-title"></span>';
                    contentHtml += '      <span class="layui-breadcrumb pull-right">';
                    contentHtml += '         <a ew-href="' + homeUrl + '">首页</a>';
                    contentHtml += '         <a><cite></cite></a>';
                    contentHtml += '      </span>';
                    contentHtml += '   </div>';
                    contentHtml += '   <div style="-webkit-overflow-scrolling: touch;">';
                    contentHtml += '      <iframe lay-id="' + menuPath + '" src="' + menuPath + '" frameborder="0" class="admin-iframe"></iframe>';
                    contentHtml += '   </div>';
                    $(bodyDOM).html(contentHtml);
                    if (menuPath != homeUrl) {
                        index.setTabTitle(menuName);
                    }
                    element.render('breadcrumb');
                } else {
                    $contentDom.attr('lay-id', menuPath);
                    $contentDom.attr('src', menuPath);
                    index.setTabTitle(menuName);
                }
                admin.activeNav(menuPath);  // 设置导航栏选中
                // 记忆选项卡
                index.mTabList.splice(0, index.mTabList.length);  // 单标签时先清空
                if (menuPath != homeUrl) {
                    index.mTabList.push(param);
                    index.mTabPosition = menuPath;  // 记录当前Tab位置
                } else {
                    index.mTabPosition = undefined;
                }
                if (index.cacheTab) {
                    admin.putTempData('indexTabs', index.mTabList);
                    admin.putTempData('tabPosition', index.mTabPosition);
                }
            }
            // 移动设备切换页面隐藏侧导航
            if (admin.getPageWidth() <= 768) {
                admin.flexible(true);
            }
        },
        /* 加载主页 */
        loadHome: function (param) {
            homeUrl = param.menuPath;  // 记录主页的地址
            var indexTabs = admin.getTempData('indexTabs');
            var tabPosition = admin.getTempData('tabPosition');
            var loadSetting = (param.loadSetting == undefined ? true : param.loadSetting);
            index.loadView({
                menuPath: homeUrl,
                menuName: param.menuName
            });
            if (!index.pageTabs) {
                admin.activeNav(param.menuPath);  // 设置导航栏选中
            }
            if (loadSetting) {
                index.loadSettings(indexTabs, tabPosition, param.onlyLast);
            }
        },
        /* 打开新页面 */
        openTab: function (param) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.index) {
                    top.layui.index.openTab(param);
                    return;
                }
            }
            var url = param.url;
            var title = param.title;
            if (param.end) {
                tabEndCall[url] = param.end;
            }
            index.loadView({
                menuPath: url,
                menuName: title
            });
        },
        /* 关闭选项卡 */
        closeTab: function (url) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.index) {
                    top.layui.index.closeTab(url);
                    return;
                }
            }
            element.tabDelete(tabFilter, url);
        },
        /* 加载设置 */
        loadSettings: function (cacheTabs, cacheTabPosition, onlyLast) {
            // 恢复记忆的tab选项卡
            if (index.cacheTab) {
                var indexTabs = cacheTabs;
                var tabPosition = cacheTabPosition;
                if (indexTabs) {
                    var mi = -1;
                    for (var i = 0; i < indexTabs.length; i++) {
                        if (index.pageTabs && !onlyLast) {
                            index.loadView(indexTabs[i]);
                        }
                        if (indexTabs[i].menuPath == tabPosition) {
                            mi = i;
                        }
                    }
                    if (mi != -1) {
                        setTimeout(function () {
                            index.loadView(indexTabs[mi]);
                            if (!index.pageTabs) {
                                admin.activeNav(tabPosition);
                            }
                        }, 150);
                    }
                }
            }
            // 读取本地配置
            var cacheSetting = layui.data(admin.tableName);
            if (cacheSetting) {
                // 是否开启footer
                if (cacheSetting.openFooter != undefined && cacheSetting.openFooter == false) {
                    $('body.layui-layout-body').addClass('close-footer');
                }
                // 是否开启tab自动刷新
                if (cacheSetting.tabAutoRefresh) {
                    $(tabDOM).attr('lay-autoRefresh', 'true');
                }
                // 设置导航小箭头
                if (cacheSetting.navArrow != undefined) {
                    $(sideDOM + '>.layui-nav-tree').removeClass('arrow2 arrow3');
                    cacheSetting.navArrow && $(sideDOM + '>.layui-nav-tree').addClass(cacheSetting.navArrow);
                }
            }
        },
        /* 设置是否记忆Tab */
        setTabCache: function (isCache) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.index) {
                    top.layui.index.setTabCache(isCache);
                    return;
                }
            }
            layui.data(admin.tableName, {key: 'cacheTab', value: isCache});
            index.cacheTab = isCache;
            if (isCache) {
                admin.putTempData('indexTabs', index.mTabList);
                admin.putTempData('tabPosition', index.mTabPosition);
            } else {
                admin.putTempData('indexTabs', []);
                admin.putTempData('tabPosition', undefined);
            }
        },
        /* 清除选项卡记忆 */
        clearTabCache: function () {
            admin.putTempData('indexTabs', undefined);
        },
        /* 设置Tab标题 */
        setTabTitle: function (title, tabId) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.index) {
                    top.layui.index.setTabTitle(title, tabId);
                    return;
                }
            }
            if (!index.pageTabs) {
                if (title) {
                    $(titleDOM).addClass('show');
                    var $titleTvDom = $(titleDOM + '>.layui-body-header-title');
                    $titleTvDom.html(title);
                    $titleTvDom.next('.layui-breadcrumb').find('cite').last().text(title);
                } else {
                    $(titleDOM).removeClass('show');
                }
            } else {
                title || (title = '');
                tabId || (tabId = $(tabDOM + '>.layui-tab-title>li.layui-this').attr('lay-id'));
                tabId && $(tabDOM + '>.layui-tab-title>li[lay-id="' + tabId + '"] .title').html(title);
            }
        },
        /* 自定义Tab标题 */
        setTabTitleHtml: function (html) {
            if (window != top && !admin.isTop()) {
                if (top.layui && top.layui.index) {
                    top.layui.index.setTabTitleHtml(html);
                    return;
                }
            }
            if (!index.pageTabs) {
                if (html) {
                    $(titleDOM).addClass('show');
                    $(titleDOM).html(html);
                } else {
                    $(titleDOM).removeClass('show');
                }
            }
        },
        /* 废弃方法兼容 */
        closeTabCache: function () {
            console.warn('closeTabCache() has been deprecated, please use clearTabCache().');
            index.clearTabCache();
        },
        /* 废弃方法兼容 */
        loadSetting: function () {
            console.warn('loadSetting() has been deprecated.');
        }
    };

    /** 读取本地缓存的设置 */
    var cacheSetting = layui.data(admin.tableName);
    if (cacheSetting) {
        if (cacheSetting.openTab != undefined) {
            index.pageTabs = cacheSetting.openTab;
        }
        if (cacheSetting.cacheTab != undefined) {
            index.cacheTab = cacheSetting.cacheTab;
        }
    }

    /** 移动设备遮罩层 */
    var siteShadeDom = '.layui-layout-admin .site-mobile-shade';
    if ($(siteShadeDom).length <= 0) {
        $('.layui-layout-admin').append('<div class="site-mobile-shade"></div>');
    }
    $(siteShadeDom).click(function () {
        admin.flexible(true);
    });

    /** 补充tab的dom */
    if (index.pageTabs && $(tabDOM).length <= 0) {
        var tabDomHtml = '<div class="layui-tab" lay-allowClose="true" lay-filter="admin-pagetabs">';
        tabDomHtml += '       <ul class="layui-tab-title"></ul>';
        tabDomHtml += '      <div class="layui-tab-content"></div>';
        tabDomHtml += '   </div>';
        tabDomHtml += '   <div class="layui-icon admin-tabs-control layui-icon-prev" ew-event="leftPage"></div>';
        tabDomHtml += '   <div class="layui-icon admin-tabs-control layui-icon-next" ew-event="rightPage"></div>';
        tabDomHtml += '   <div class="layui-icon admin-tabs-control layui-icon-down">';
        tabDomHtml += '      <ul class="layui-nav admin-tabs-select" lay-filter="admin-pagetabs-nav">';
        tabDomHtml += '         <li class="layui-nav-item" lay-unselect>';
        tabDomHtml += '            <a></a>';
        tabDomHtml += '            <dl class="layui-nav-child layui-anim-fadein">';
        tabDomHtml += '               <dd ew-event="closeThisTabs" lay-unselect><a>关闭当前标签页</a></dd>';
        tabDomHtml += '               <dd ew-event="closeOtherTabs" lay-unselect><a>关闭其它标签页</a></dd>';
        tabDomHtml += '               <dd ew-event="closeAllTabs" lay-unselect><a>关闭全部标签页</a></dd>';
        tabDomHtml += '            </dl>';
        tabDomHtml += '         </li>';
        tabDomHtml += '      </ul>';
        tabDomHtml += '   </div>';
        $(bodyDOM).html(tabDomHtml);
        element.render('nav');
    }

    /** 监听侧导航栏点击事件 */
    element.on('nav(' + navFilter + ')', function (elem) {
        var $that = $(elem);
        var menuUrl = $that.attr('lay-href');
        var menuId = $that.attr('lay-id');
        if (!menuId) {
            menuId = menuUrl;
        }
        if (menuUrl && menuUrl != 'javascript:;') {
            var menuName = $that.attr('ew-title');
            menuName || (menuName = $that.text().replace(/(^\s*)|(\s*$)/g, ''));  // 去空格
            index.loadView({
                menuId: menuId,
                menuPath: menuUrl,
                menuName: menuName
            });
        }
        // 手风琴侧边栏(已由element模块实现)
        /*if ('true' == ($(sideDOM + '>.layui-nav-tree').attr('lay-accordion'))) {
            if (($that.parent().hasClass('layui-nav-itemed')) || ($that.parent().hasClass('layui-this'))) {
                $(sideDOM + '>.layui-nav .layui-nav-itemed').not($that.parents('.layui-nav-child').parent()).removeClass('layui-nav-itemed');
                $that.parent().addClass('layui-nav-itemed');
            }
            $that.trigger('mouseenter');
        }*/
    });

    /** tab选项卡切换监听 */
    element.on('tab(' + tabFilter + ')', function (data) {
        var layId = $(this).attr('lay-id');

        // 记录当前Tab位置
        if (layId != homeUrl) {
            index.mTabPosition = layId;
        } else {
            index.mTabPosition = undefined;
        }
        if (index.cacheTab) {
            admin.putTempData('tabPosition', index.mTabPosition);
        }
        admin.rollPage('auto');  // 自动滚动
        admin.activeNav(layId);  // 设置导航栏选中

        // 解决切换tab滚动条时而消失的问题
        /*var $iframe = $(tabDOM + '>.layui-tab-content>.layui-tab-item.layui-show .admin-iframe')[0];
        if ($iframe) {
            $iframe.focus();
            admin.hideTableScrollBar($iframe.contentWindow);
            $iframe.style.height = "99%";
            $iframe.scrollWidth;
            $iframe.style.height = "100%";
            $($iframe.contentWindow).resize();
        }*/
        // 切换tab自动刷新
        var autoRefresh = $(tabDOM).attr('lay-autoRefresh');
        if (autoRefresh === 'true' && !mIsAddTab) {  // 第一次打开Tab不刷新
            admin.refresh(layId);
        }
        mIsAddTab = false;
    });

    /** tab选项卡删除监听 */
    element.on('tabDelete(' + tabFilter + ')', function (data) {
        var mTab = index.mTabList[data.index - 1];
        if (mTab) {
            var layId = mTab.menuPath;
            index.mTabList.splice(data.index - 1, 1);
            if (index.cacheTab) {
                admin.putTempData('indexTabs', index.mTabList);
            }
            if (tabEndCall[layId]) {
                tabEndCall[layId].call();
            }
        }
        // 解决偶尔出现关闭后没有选中任何Tab的bug
        if ($(tabDOM + '>.layui-tab-title>li.layui-this').length <= 0) {
            $(tabDOM + '>.layui-tab-title>li:last').trigger('click');
        }
    });

    /** 多系统切换事件 */
    $(document).off('click.navMore').on('click.navMore', '[nav-bind]', function () {
        var navId = $(this).attr('nav-bind');
        $('ul[lay-filter="' + navFilter + '"]').addClass('layui-hide');
        $('ul[nav-id="' + navId + '"]').removeClass('layui-hide');
        if (admin.getPageWidth() <= 768) {
            admin.flexible(false);  // 展开侧边栏
        }
        $(headerDOM + '>.layui-nav .layui-nav-item').removeClass('layui-this');
        $(this).parent('.layui-nav-item').addClass('layui-this');
    });

    /** 开启Tab右键菜单 */
    if (index.openTabCtxMenu && index.pageTabs) {
        layui.use('contextMenu', function () {
            var contextMenu = layui.contextMenu;
            if (contextMenu) {  // contextMenu模块可能会不存在
                $(tabDOM + '>.layui-tab-title').off('contextmenu.tab').on('contextmenu.tab', 'li', function (e) {
                    var layId = $(this).attr('lay-id');
                    contextMenu.show([{
                        icon: 'layui-icon layui-icon-refresh',
                        name: '刷新当前',
                        click: function () {
                            element.tabChange(tabFilter, layId);
                            var autoRefresh = $(tabDOM).attr('lay-autoRefresh');
                            if (!autoRefresh || autoRefresh !== 'true') {
                                admin.refresh(layId);
                            }
                        }
                    }, {
                        icon: 'layui-icon layui-icon-close-fill ctx-ic-lg',
                        name: '关闭当前',
                        click: function () {
                            admin.closeThisTabs(layId);
                        }
                    }, {
                        icon: 'layui-icon layui-icon-unlink',
                        name: '关闭其他',
                        click: function () {
                            admin.closeOtherTabs(layId);
                        }
                    }, {
                        icon: 'layui-icon layui-icon-close ctx-ic-lg',
                        name: '关闭全部',
                        click: function () {
                            admin.closeAllTabs();
                        }
                    }], e.clientX, e.clientY);
                    return false;
                });
            }
        });
    }

    exports('index', index);
});
