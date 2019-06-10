這是關於學習JAVA ZK程式碼分享

基於內部使用，JAVA CODE 有包含不分享的底層Class，所以一般人是無法直接由該程式碼得到結果。
請考慮是否參考

### 2019/05/30(作業8-異動)
#### 一、修正
1. 多國語變數定義的變數命名規則.
2. BrandEgriadController 變數命名規則修正(BNo,BName).
3. UK控制寫到TrainingGeneralListboxAndEgridActionHandler，改回到BrandEgridCotroller
#### 二、增加
1. onBeforeSave在BrandEgridCotroller，移除constraint在brandcode上.
2. jhensin.msg.checkInputboandcode訊息多國語

### 2019/06/03(作業9部份完成，明細列表、編輯鈕、DAO設定)
1. 多國語系檔新增定義(BRAND_SEASON)
2。 增加BrandSeasonDAO、修正TPDAOFactory內容
3. brand_egrid.zul增加明細檔部份
4. BrandEgridController增加.seasonActionHandler

### 2019/06/04(作業9完成)
1. 多國語系檔新增定義(jhensin.msg.checkInputboandcode,jhensin.msg.hasDetailError)
2. 當有明細時，不可刪除主檔
3. 資料來源請按季度排序(升冪)
4. 明細欄位季度：固定大寫
5. 明細加上加上一個 UK 管控(BRAND_ID + SEASON_NO)
6. 明細月份起迄兩欄，請用 spinner 元件，並加 constraint 控制只能輸入 1 ~ 12

### 2019/06/04(作業10完成)
1. 排版：主明細各佔一半高度，並讓 Elistbox 可出現垂直 Scrollbar
2. 加上兩個 UK 管控：BRAND_NO,BRAND_CODE
3. 存檔前，檢查 BRAND_CODE 必須是 2 碼英數字
4. 當有明細時，不可刪除主檔
5. 明細 資料來源請按季度排序(升冪)
6. 季度：固定大寫
7. 加上一個 UK 管控： BRAND_ID + SEASON_NO
8. 月份起迄兩欄，請用 spinner 元件，並加 constraint 控制只能輸入 1 ~ 12

### 2019/06/10(作業10修正)
1. brand_egrid.zul的部案
    1. 主檔的 BRAND_NO 欄沒有固定大寫
    2. 主檔的 BRAND_CODE 欄，可以加上 maxlength="2"
    3. 明細的 SEASON_NO 欄，沒有管控只有新增才能編輯
    4. 明細的月份兩欄，多了 no empty 的管控
2. BrandEgridCotroller的檔案
    1. 查詢的判斷邏輯有問題，當只有填一個查詢條件時，會查出全部的資料
    2. 新增季度時，若輸入重複的代號，存檔時的提示訊息會含有 【BRAND_ID】=xxx(UUID 22 碼)，應設法去除
    3. onBeforeDelete 中目前是取回 MapBeanResultList 來判斷筆數，可以改為 SQL 直接只取回筆數 (SELECT COUNT)，會比較有效率
3. brand_elistbox.zul
    1. 主明細各一半高度的排版，目前你的處理方式較不好，當資料多時，scrollbar 會是在 div 上，整個 panel 會被捲動
    2. 查詢條件多了 constraint="no empty"，onQuery 也請記得一併調整正確
    3. 明細的 SEASON_NO 欄，沒有管控只有新增才能編輯
    4. 明細的月份兩欄，多了 no empty 的管控
4. BrandElistboxController
    1. 建議先全部 setActionHandler() 之後，才開始載入資料，若有其他元件設定之類的，最好也都在載資料前處理，以免有什麼問題
    2. String QT，String FR，String BName，String BNo，名稱不符合命名原則
    3. checkBrandCode()，若要將邏輯抽出成為一個 method，最好練習低耦合度的設計，目前檢查的資料是用傳入的(低耦合)，但 method 內卻與該欄位有高耦合的關係，這樣不好
    4. onBeforeDelete 中目前是取回 MapBeanResultList 來判斷筆數，可以改為 SQL 直接只取回筆數 (SELECT COUNT)，會比較有效率
