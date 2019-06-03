這是關於學習JAVA ZK程式碼分享

基於內部使用，JAVA CODE 有包含不分享的底層Class，所以一般人是無法直接由該程式碼得到結果。
請考慮是否參考

###2019/05/30(作業8-異動)
#### 一、修正
1. 多國語變數定義的變數命名規則.
2. BrandEgriadController 變數命名規則修正(BNo,BName).
3. UK控制寫到TrainingGeneralListboxAndEgridActionHandler，改回到BrandEgridCotroller
#### 二、增加
1. onBeforeSave在BrandEgridCotroller，移除constraint在brandcode上.
2. jhensin.msg.checkInputboandcode訊息多國語

###2019/06/03(作業9部份完成，明細列表、編輯鈕、DAO設定)
1. 多國語系檔新增定義(BRAND_SEASON)
2。 增加BrandSeasonDAO、修正TPDAOFactory內容
3. brand_egrid.zul增加明細檔部份
4. BrandEgridController增加.seasonActionHandler