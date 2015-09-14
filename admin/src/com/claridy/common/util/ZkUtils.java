package com.claridy.common.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.GenericEventListener;
import org.zkoss.zk.ui.sys.ExecutionCtrl;
import org.zkoss.zk.ui.sys.SessionCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.impl.InputElement;

/**
 * ZkUtils集合了zk中常用的一些功能，方便開發中的使用
 * 
 * 
 * @author sunflower
 * 
 * 
 *         Email:zhangxuehuaemail # gmail 點 com
 * @since zk5.0+
 */
public class ZkUtils {
	/**
	 * 
	 * 獲得當前Execution
	 * <p>
	 * 客戶端請求的執行器（例如ServletRequest），當客戶端發送一個請求後，
	 * 服務端負責構造這個Execution對象，獲取當前執行的資訊，然後服務這個請求
	 * <p>
	 * 一個客戶端請求, 例如, HttpServletRequest, 或許關聯多個請求 request (
	 * {@link org.zkoss.zk.au.AuRequest}). 但是, 這些 ZK 請求來自的頁面 ({@link Page}
	 * )必須具有相同的desktop
	 * 
	 * <p>
	 * 因為一個請求也許來自http或其他協議，Execution通常一個隔離層
	 * 
	 * 
	 * @return 當前execution
	 * @see Execution
	 */
	public static final Execution getCurrentExecution() {
		return Executions.getCurrent();
	}

	/**
	 * 返回當前Exection桌面對象
	 * 
	 * @return
	 * @see Execution#getDesktop()
	 */
	public static final Desktop getCurrentDesktop() {
		return getCurrentExecution().getDesktop();
	}

	/**
	 * 獲得當前Execution所屬的會話
	 * <p>
	 * <b>注</b>該Session不同於HttpSession,該session為zk定義的session作用域
	 * 
	 * @return
	 * @see Session
	 */
	public static final Session getCurrentSession() {
		return Sessions.getCurrent();
	}

	/**
	 * 返回本地session對象，如果不可用返回null,返回的對象依賴客戶端類型，如果是就http的
	 * ，那麼返回javax.servlet.http.HttpSession的一個實例
	 * ，如果是portlet，那麼返回javax.portlet.PortletSession的實例
	 */
	public static final Object getNativeSession() {
		return getCurrentSession().getNativeSession();
	}

	/**
	 * 返回本地請求對象，如果不可用返回null
	 * 
	 * @return 返回的對象依賴web容器，如果web容器時一個servlet容器，那麼返回的對象為ServletRequest
	 */
	public static final Object getNativeRequest() {
		return getCurrentExecution().getNativeRequest();
	}

	/**
	 * 返回本地回應對象，如果不可用返回null
	 * 
	 * @return 返回的對象依賴web容器，如果web容器時一個servlet容器，那麼返回的對象為ServletResponse
	 */
	public static final Object getNativeResponse() {
		return getCurrentExecution().getNativeResponse();
	}

	/**
	 * 獲得WebApp對象
	 * 
	 * @return
	 * @see Desktop#getWebApp()
	 * @see WebApp
	 */
	public static final WebApp getWebApp() {
		return getCurrentDesktop().getWebApp();
	}

	public static final String getRealPath(String path) {
		return getWebApp().getRealPath(path);
	}

	/**
	 * 獲得當前請求來自的頁面
	 * 
	 * @return
	 */
	public static final Page getCurrentPage() {
		return ((ExecutionCtrl) getCurrentExecution()).getCurrentPage();
	}

	/**
	 * 返回execution作用域內參數
	 * 
	 * @return
	 * @see Execution#getArg()
	 */
	@SuppressWarnings("rawtypes")
	public static final Map getExecutionArgs() {
		return getCurrentExecution().getArg();
	}

	/**
	 * 獲得當前Execution作用域內的屬性
	 * 
	 * @see Execution#getAttributes()
	 */
	@SuppressWarnings("rawtypes")
	public static final Map getExectionAttributes() {
		return getCurrentExecution().getAttributes();
	}

	/**
	 * 設置請求屬性值
	 * 
	 * @param name
	 *            請求屬性
	 * @param value
	 *            屬性值
	 */
	public static final void setExecutionAttribute(String name, Object value) {
		getCurrentExecution().setAttribute(name, value);
	}

	/**
	 * 設置Execution作用域屬性值或其父作用域的值
	 * 
	 * @param name
	 *            請求屬性
	 * @param value
	 *            屬性值
	 * @param recurse
	 *            檢查父作用域是否存在該屬性，如果存在將替換父作用域的值
	 * 
	 */
	public static final void setExecutionAttribute(String name, Object value,
			boolean recurse) {
		getCurrentExecution().setAttribute(name, value, recurse);
	}

	/**
	 * 獲得請求參數
	 * 
	 * @return 參數map
	 */
	@SuppressWarnings("rawtypes")
	public static final Map getExecutionParameterMap() {
		return getCurrentExecution().getParameterMap();
	}

	/**
	 * 獲得請求參數值
	 * 
	 * @param name
	 *            請求參數的名字
	 * @return 指定名字的參數值
	 */
	public static final String getExecutionParameter(String name) {
		return getCurrentExecution().getParameter(name);
	}

	/**
	 * 獲得請求參數值
	 * 
	 * @param name
	 *            參數的名字
	 * @return 字元數組
	 */
	public static final String[] getExecutionParameterValues(String name) {
		return getCurrentExecution().getParameterValues(name);
	}

	/**
	 * 獲得當前請求消息頭
	 * 
	 * @param name
	 *            消息頭名字
	 * @return 消息頭值
	 */
	public static final String getRequestHeader(String name) {
		return getCurrentExecution().getHeader(name);
	}

	/**
	 * 添加一個指定名稱和值的相應頭，允許相應頭具有多值
	 * 
	 * @param name
	 * @param value
	 */
	public static final void addResponseHeader(String name, String value) {
		getCurrentExecution().addResponseHeader(name, value);
	}

	/**
	 * 添加一個指定名稱和日期值的回應頭
	 * 
	 * @param name
	 * @param value
	 */
	public static final void addResponseHeader(String name, Date value) {
		getCurrentExecution().addResponseHeader(name, value);
	}

	/**
	 * 返回接收請求的本地ip地址
	 * 
	 * @return
	 */
	public static final String getLocalAddr() {
		return getCurrentExecution().getLocalAddr();
	}

	/**
	 * 獲得接收請求的本地host name
	 * 
	 * @return
	 */
	public static final String getLocalName() {
		return getCurrentExecution().getLocalName();
	}

	/**
	 * 獲得接收請求的本地端口
	 * 
	 * @return
	 */
	public static final int getLocalPort() {
		return getCurrentExecution().getLocalPort();
	}

	/**
	 * 獲得發送請求的客戶端ip
	 * 
	 * @return
	 */
	public static final String getRemoteAddr() {
		return getCurrentExecution().getRemoteAddr();
	}

	/**
	 * 獲得發送請求的客戶端的host name
	 * 
	 * @return
	 */
	public static final String getRemoteHost() {
		return getCurrentExecution().getRemoteHost();
	}

	/**
	 * 設置session 屬性
	 * 
	 * @param name
	 *            屬性名
	 * @param value
	 *            屬性值
	 */
	public static final void setSessionAttribute(String name, Object value) {
		getCurrentSession().setAttribute(name, value);
	}

	/**
	 * 設置session或父作用域 屬性
	 * 
	 * @param name
	 *            屬性名
	 * @param value
	 *            屬性值
	 * @param recurse
	 *            是否查詢父作用域包含name名字的屬性，如果包含將替換該值
	 */
	public static final void setSessionAttribute(String name, Object value,
			boolean recurse) {
		getCurrentSession().setAttribute(name, value, recurse);
	}

	/**
	 * 返回session作用域對象
	 * 
	 * @param name
	 *            屬性名
	 * @param recurse
	 *            是否檢索父作用域，如果為true， 並且當前作用域沒聲明這個屬性，那麼將搜索父作用域
	 * @return
	 */
	public static final Object getSessionAttribute(String name, boolean recurse) {
		return getCurrentSession().getAttribute(name, recurse);
	}

	/**
	 * 返回session作用域對象
	 * 
	 * @param name
	 *            屬性名
	 * @return
	 */
	public static final Object getSessionAttribute(String name) {
		return getCurrentSession().getAttribute(name);
	}

	/**
	 * 獲得所有session作用域對象
	 * 
	 * @return map 類型的作用域所有對象
	 */
	@SuppressWarnings("rawtypes")
	public static final Map getSessionAttributes() {
		return getCurrentSession().getAttributes();
	}

	/**
	 * 獲得會話超時事件，單位秒
	 * 
	 * @return
	 */
	public static final int getSessionMaxInactiveInterval() {
		return getCurrentSession().getMaxInactiveInterval();
	}

	/**
	 * 指定失效事件，單位秒，負值表示永不過期
	 * 
	 * @param interval
	 */
	public static final void setSessionMaxInactiveInterval(int interval) {
		getCurrentSession().setMaxInactiveInterval(interval);
	}

	/**
	 * 銷毀當前session
	 * <p>
	 * 
	 * 表示解除綁定在session上的所有對象 注意: 通常你使用 {@link Executions#sendRedirect}
	 * 讓客戶端重定向另一個頁面(或重加載同一頁面) session並不立即銷毀 ，而是在當前請求之後銷毀，即重定向頁面顯示完畢之後
	 * 如果想立即銷毀((SessionCtrl)Sessions.getCurrent()).invalidateNow();
	 * 在zk中這一點和通常所說的SttpSession.invalidate()有所不同
	 * <p>
	 * 由 天明ゞ破曉 (qq 513062844) 發現。great thanks
	 */
	public static final void invalidateSession() {
		getCurrentSession().invalidate();
	}

	/**
	 * 立即銷毀當前session
	 * <p>
	 * 非立即銷毀session情況見 {@link #invalidateSession()}
	 */
	public static final void invlidateSessionNow() {
		((SessionCtrl) getCurrentSession()).invalidateNow();
	}

	/**
	 * 設置頁面作用域屬性
	 * 
	 * @param name
	 *            屬性名
	 * @param value
	 *            屬性值
	 */
	public static final void setPageAttribute(String name, Object value) {
		getCurrentPage().setAttribute(name, value);
	}

	/**
	 * 
	 * 設置page或父作用域屬性
	 * 
	 * @param name
	 * @param value
	 * @param recurse
	 *            是否檢索父作用域，如果為true， 並且當前作用域沒聲明這個屬性，那麼將搜索父作用域 ,並替換
	 */
	public static final void setPageAttribute(String name, Object value,
			boolean recurse) {
		getCurrentPage().setAttribute(name, value, recurse);
	}

	/**
	 * 獲得當前請求來自的頁面
	 * 
	 * @return
	 */
	public static final String getRequestPagePath() {
		return getCurrentPage().getRequestPath();
	}

	/**
	 * 獲得桌面作用域屬性
	 * 
	 * @param name
	 * @return
	 */
	public static final Object getDesktopAttribute(String name) {
		return getCurrentDesktop().getAttribute(name);
	}

	/**
	 * 獲得指定id的桌面
	 * <p>
	 * 需要注意到是：例如在page1中包含iframe，iframe包含的頁面為page2，那麼zk將為page2新建一個桌面對象desktop2，
	 * 因此page1與page2屬於不同的桌面， 當你在page2的一個按鈕或所屬的其他組件觸發的事件中
	 * 使用該方法獲得page1的子頁面的時候，當前動作請求所屬桌面為desktop2,而不是page1所屬的desktop1，
	 * 因此你無法從desktop2中查找屬於desktop1的頁面
	 * 
	 * @param pageId
	 *            頁面的id
	 * @return 頁面對戲那個
	 * @see Desktop#getPage(String)
	 */
	public static final Page getPage(String pageId) {
		return getCurrentDesktop().getPage(pageId);
	}

	public static final void forward(String page) throws IOException {
		Executions.forward(page);
	}

	public static final void sendRedirect(String page) {
		Executions.sendRedirect(page);
	}

	/**
	 * 向當前execution提交一個事件
	 * <p>
	 * 將事件提交到事件佇列末尾，然後立即返回。 佇列中排在前面的事件處理完畢後執行該動作提交的事件。
	 * 
	 * @param event
	 */
	public static final void postEvent(Event event) {
		Events.postEvent(event);
	}

	/**
	 * 向當前execution提交一個事件，可以設置事件的優先順序
	 * <p>
	 * 將事件提交到事件佇列末尾，然後立即返回。 佇列中排在前面的事件處理完畢後執行該動作提交的事件。
	 * 
	 * @priority
	 * @param event
	 */
	public static final void postEvent(int priority, Event event) {
		Events.postEvent(priority, event);

	}

	/**
	 * 向目標組件發送指定名稱的事件
	 * <p>
	 * 將事件提交到事件佇列末尾，然後立即返回。 佇列中排在前面的事件處理完畢後執行該動作提交的事件。
	 */
	public static final void postEvent(String name, Component target,
			Object data) {
		Events.postEvent(name, target, data);
	}

	/**
	 * 向當前execution發送一個事件
	 * <p>
	 * 事件處理線程和調用該方法的線程為同一線程，即二者為相同線程，所以必須等待事件處理完畢，該方法才會返回。
	 * <p>
	 * 如果目標事件的的處理器，是一個長操作，那麼當前線程將長事件阻塞，而在客戶端表現為：左上角一直出現"正在處理，請稍候..."等字樣的提示，
	 * 所以在使用前注意
	 * 
	 * @param event
	 */
	public static final void sendEvent(Event event) {
		Events.sendEvent(event);
	}

	/**
	 * 向指定組件發送事件
	 * <p>
	 * 事件處理線程和調用該方法的線程為同一線程，即二者為相同線程，所以必須等待事件處理完畢，該方法才會返回
	 * <p>
	 * 如果目標事件的的處理器，是一個長操作，那麼當前線程將長事件阻塞，而在客戶端表現為：左上角一直出現"正在處理，請稍候..."等字樣的提示，
	 * 所以在使用前注意
	 * 
	 * @param comp
	 *            目標組件
	 * @param event
	 */
	public static final void sendEvent(Component comp, Event event) {
		Events.sendEvent(comp, event);
	}

	/**
	 * 向目標組件發送指定名稱的事件
	 * 
	 * <p>
	 * 事件處理線程和調用該方法的線程為同一線程，即二者為相同線程，所以必須等待事件處理完畢，該方法才會返回
	 * <p>
	 * 如果目標事件的的處理器，是一個長操作，那麼當前線程將長事件阻塞，而在客戶端表現為：左上角一直出現"正在處理，請稍候..."等字樣的提示，
	 * 所以在使用前注意
	 * 
	 * @param name
	 *            事件名稱
	 * @param target
	 *            目標組件
	 * @param data
	 *            事件攜帶的數據，可以調用在事件監聽器中使用<code>Event.getData()</code>獲得該數據
	 */
	public static final void sendEvent(String name, Component target,
			Object data) {
		Events.sendEvent(name, target, data);
	}

	public static final Event getRealOrigin(ForwardEvent event) {
		return Events.getRealOrigin(event);
	}

	/**
	 * <p>
	 * 給指定的組件添加controller對象中定義的onXxx事件處理器，該controller是一個
	 * 包含onXxx方法的POJO對象，該工具方法將onXxx方法註冊給指定組件，因此你不用通過{@link EventListener}
	 * 一個一個的向組件註冊了
	 * </p>
	 * 
	 * <p>
	 * 所有在controller對象中以"on"開頭的公共方法被作為事件處理器，並且相關事件同時也被監聽，例如，
	 * 如果controller對象有一個名字為onOk的方法，那麼 onOk事件將被監聽，然後當接收到onOk事件的時候， onOk方法被調用
	 * 
	 * @param comp
	 *            the component to be registered the events
	 * @param controller
	 *            a POJO file with onXxx methods(event handlers)
	 * @since 3.0.6
	 * @see GenericEventListener
	 */
	public static final void addEventListeners(Component comp, Object controller) {
		Events.addEventListeners(comp, controller);
	}

	/**
	 * 檢測名稱時候一個合法的zk事件名
	 * 
	 * @param name
	 * @return
	 */
	public static final boolean isValidEventName(String name) {
		return Events.isValid(name);
	}

	/**
	 * 判斷一個指定事件的組件是否有事件處理器或監聽器
	 * 
	 * @param comp
	 * @param evtnm
	 * @param asap
	 *            是否僅檢測非延遲事件監聽器，例如實現org.zkoss.zk.ui.event.Deferrable或
	 *            org.zkoss.zk.ui.event.Deferrable.isDeferrable 返回 false的監聽器
	 * @return
	 */
	public static final boolean isListened(Component comp, String evtnm,
			boolean asap) {
		return Events.isListened(comp, evtnm, asap);
	}

	/**
	 * 從uri指定的檔創建組件
	 * 
	 * @param uri
	 * @param parent
	 *            創建的組件所屬的父組件F
	 * @param args
	 *            創建組件傳遞的參數
	 * @return 創建的組件，該組件對象為uri頁面的第一個組件（zk節點除外）
	 */
	public static final Component createComponents(String uri,
			Component parent, @SuppressWarnings("rawtypes") Map args) {
		return Executions.createComponents(uri, parent, args);
	}

	/**
	 * 從zul格式字串創建組件
	 * 
	 * @param content
	 *            zul格式內容的字串
	 * @param parent
	 *            父組件，如果為null,那麼組件所屬的頁面為當前頁面，當前頁面由execution上下文決定。
	 *            另外新的組件將作為當前頁面的根組件
	 * @param args
	 *            一個map類型的參數， 傳遞的參數可以使用Executions.getArgs()獲得
	 * @return 根據content創建的組件第一個組件
	 */
	public static final Component createComponentsDirectly(String content,
			Component parent, @SuppressWarnings("rawtypes") Map args) {
		return Executions
				.createComponentsDirectly(content, "zul", parent, args);
	}

	/**
	 * 重繪組件
	 * <p>
	 * 僅允許在<b>請求處理階段</b>和<b>事件處理階段</b>調用， 不允許在<b>呈現階段</b>調用
	 * 
	 * @param comp
	 */
	public static final void redraw(Component comp) {
		comp.invalidate();
	}

	/**
	 * 重繪頁面
	 * <p>
	 * 僅允許在<b>請求處理階段</b>和<b>事件處理階段</b>調用， 不允許在<b>呈現階段</b>調用
	 * 
	 * @param page
	 */
	public static final void redrawPage(Page page) {
		page.invalidate();
	}

	/**
	 * 彈出消息提示框
	 * 
	 * @param message
	 *            提示消息
	 * @param title
	 *            提示框標題
	 */
	public static final void showInformation(String message, String title) {
		Messagebox.show(message, title, Messagebox.OK, Messagebox.INFORMATION);
	}

	/**
	 * 彈出警告提示框
	 * 
	 * @param message
	 *            提示消息
	 * @param title
	 *            提示框標題
	 */
	public static final void showExclamation(String message, String title) {
		Messagebox.show(message, title, Messagebox.OK, Messagebox.EXCLAMATION);
	}

	/**
	 * 彈出消息提示框
	 * 
	 * @param message
	 *            提示消息
	 * @param title
	 *            提示框標題
	 */
	public static final void showError(String message, String title) {
		Messagebox.show(message, title, Messagebox.OK, Messagebox.ERROR);
	}

	/**
	 * 詢問提示框
	 * <p>
	 * 如果禁用事件處理線程，該方法會立即返回，返回值永遠為true。 如果作為if判斷語句的條件，
	 * 那麼else部分永遠不會執行，啟用和開啟事件處理請查看zk.xml配置: <br />
	 * &lt;system-config&gt;<br />
	 * &lt;disable-event-thread&gt;false&lt;/disable-event-thread&gt;<br />
	 * &lt;/system-config&gt;
	 * 
	 * @param message
	 *            提示消息 提示框標題
	 * @return 禁用事件處理線程該方法永遠返回true，啟用事件處理相稱時，如果用戶點擊ok按鈕，返回true,反之false
	 */
	public static final boolean showQuestion(String message, String title) {
		return Messagebox.OK == Messagebox.show(message, title, Messagebox.OK
				| Messagebox.CANCEL, Messagebox.QUESTION);
	}

	/**
	 * 詢問提示框
	 * <p>
	 * 該方法是一個類似 {@link #showQuestion(String, String)}
	 * 的方法，但與其不同的是，當禁用事件處理線程時，該方法非常有用。
	 * <p>
	 * 
	 * <p>
	 * 示例:<br />
	 * <hr>
	 * 
	 * <pre>
	 * ZkUtils.showQuestion(&quot;您確定刪除該記錄嗎？&quot;, &quot;詢問&quot;, new EventListener() {
	 * 	&#064;Override
	 * 	public void onEvent(Event event) throws Exception {
	 * 		int clickedButton = (Integer) event.getData();
	 * 		if (clickedButton == Messagebox.OK) {
	 * 			// 用戶點擊的是確定按鈕
	 * 		} else {
	 * 			// 用戶點擊的是取消按鈕
	 * 		}
	 * 	}
	 * 
	 * });
	 * </pre>
	 * 
	 * <hr>
	 * <p>
	 * 
	 * <table border="1">
	 * <tr>
	 * <td>按鈕名稱</td>
	 * <td>事件名稱</td>
	 * </tr>
	 * <tr>
	 * <td>確定</td>
	 * <td>onOK</td>
	 * </tr>
	 * <tr>
	 * <td>取消</td>
	 * <td>onCancel</td>
	 * </tr>
	 * </table>
	 * 
	 * @param message
	 * @param title
	 * @param eventListener
	 */
	public static final void showQuestion(String message, String title,
			EventListener eventListener) {
		Messagebox.show(message, title, Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, eventListener);

	}

	/**
	 * 給指定組件添加錯誤提示
	 * <p>
	 * 清除錯誤，需要使用{@link #clearWrongValue(Component)}
	 * 
	 * @param comp
	 * @param message
	 *            錯誤提示消息
	 * @see #clearWrongValue(Component)
	 */
	public static final void wrongValue(Component comp, String message) {
		Clients.wrongValue(comp, message);
	}

	/**
	 * 清除指定組件的錯誤提示
	 * 
	 * @param comp
	 */
	public static final void clearWrongValue(Component comp) {
		Clients.clearWrongValue(comp);
	}

	/**
	 * 清除列表中組件的錯誤提示
	 */
	public static final void clearWrongValue(
			@SuppressWarnings("rawtypes") List comps) {
		Clients.clearWrongValue(comps);

	}

	/**
	 * 設置或刪除widget的事件監聽器，如果已經有同樣的事件監聽，那麼上一個將被替換
	 * 
	 * @param comp
	 * @param evtName
	 *            事件名稱,例如onClick
	 * @param script
	 *            javascript腳本代碼，書寫格式可按照html事件中js代碼格式,如果為空，那麼事件處理程式被刪除
	 */
	public static final void setWidgetEventListener(Component comp,
			String evtName, String script) {
		comp.setWidgetListener(evtName, script);
	}

	/**
	 * 
	 * 向指定組件事件追加事件監聽器
	 * 
	 * <pre>
	 * 
	 * ZkUtils.addWidgetEventListener(txtAge, &quot;onKeyPress&quot;, &quot;&quot;
	 * 		+ &quot;    if(event.keyCode&lt;48||event.keyCode&gt;57){         &quot;
	 * 		+ &quot;       return false;                                 &quot; + &quot;     }   &quot;
	 * 		+ &quot;   &quot;);
	 * </pre>
	 * 
	 * @param comp
	 * @param evtnm
	 *            事件名稱,例如onClick
	 * @param script
	 *            javascript腳本代碼，書寫格式可按照html事件中js代碼格式
	 */
	public static final void addWidgetEventListener(Component comp,
			String evtnm, String script) {
		if (script == null || "".equals(script.trim())) {
			return;
		}
		String oldScript = comp.getWidgetListener(evtnm);
		if (oldScript == null) {
			oldScript = "";
		}
		comp.setWidgetListener(evtnm, oldScript + script);

	}

	/**
	 * 驗證表單
	 * <p>
	 * 需要input元素的constraint屬性的支持
	 * <p>
	 * 
	 * 例如 年齡&lt;textbox constraint=&quot;/^[0-9]*$/:僅允許輸入數字&quot;/&gt;
	 * 
	 * @param formContainer
	 *            Input元素公共
	 * @return 如果驗證成功返回true,否則返回false
	 */
	public static boolean validateForm(Component formContainer) {
		return validateForm(formContainer, true);
	}

	/**
	 * 驗證表單
	 * <p>
	 * 需要input元素的constraint屬性的支持
	 * 
	 * 例如 年齡 &lt;textbox constraint=&quot;/^[0-9]*$/:僅允許輸入數字&quot;/&gt;
	 * 
	 * @param formContainer
	 *            Input元素公共組件，即需要驗證的輸入元素所在的公共容器組件
	 * @param showError
	 *            是否顯示錯誤提示
	 * @return 如果驗證成功返回true,否則返回false
	 */
	public static boolean validateForm(Component formContainer,
			boolean showError) {
		try {
			validateForm0(formContainer, showError);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static void validateForm0(Component formContainer, boolean showError) {
		@SuppressWarnings("unchecked")
		List<Component> cList = formContainer.getChildren();
		if (cList == null || cList.size() < 1) {
			return;
		} else {
			for (Component c : cList) {
				if (c instanceof InputElement && !((InputElement) c).isValid()) {
					if (showError) {
						((InputElement) c).getText();
					}
					throw new RuntimeException("表單輸入不正確！");
				} else {
					validateForm0(c, showError);
				}
			}
		}
	}

	/**
	 * 結束常操作處理
	 * <p>
	 * 一個業務操作可能要一段時間可以處理完成，在處理期間，又不想讓用戶操作介面，影響業務處理等，
	 * 那麼可以在前臺事件中調用zk.startProcessing(),此時左上角出現提示框，"正在處理,請稍候..."
	 * 
	 */
	public static final void endProcessing() {
		Clients.evalJavaScript("zk.endProcessing();");
	}
	/**
	 * 點擊菜單重新加載頁面
	 * @param url
	 */
	public static void refurbishMethod(String url){
		Desktop dkp = Executions.getCurrent().getDesktop();
		Page page = dkp.getPageIfAny("templatePage");
		Include contentInclude = (Include) page.getFellowIfAny("contentInclude");
		contentInclude.setSrc("home.zul");
		contentInclude.setSrc(url);
	}
	/**
	 * 正則運算式判斷傳過來的參數是否是url格式
	 * @param title
	 * @return
	 */
	public static boolean isUrl(String title) {
	   String regex = "((http://)?([a-z]+[.])|(www.))\\w+[.]([a-z]{2,4})?[[.]([a-z]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z]{2,4}+|/?)";
	   Pattern pat = Pattern.compile(regex);
	   Matcher mat = pat.matcher(title);
	   return mat.find();
	}
	/**
	 * 正則運算式判斷傳過來的參數是否是email格式
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail){
 	    Pattern pattern = Pattern.compile(".+@.+\\..+");
 	    Matcher matcher = pattern.matcher(strEmail);
 	    return matcher.matches();
 	}
}
