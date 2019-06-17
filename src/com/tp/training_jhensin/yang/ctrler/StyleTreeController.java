package com.tp.training_jhensin.yang.ctrler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Comboitem;

import com.tp.baselib.model.MapBean;
import com.tp.baselib.model.MapBeanResultList;
import com.tp.baselib.util.ObjectUtils;
import com.tp.baselib.zul.Combobox;
import com.tp.baselib.zul.DataContainer;
import com.tp.baselib.zul.DynamicTreeNode;
import com.tp.baselib.zul.Egrid;
import com.tp.baselib.zul.InputsContainer.EditEvent;
import com.tp.baselib.zul.ListModelList;
import com.tp.baselib.zul.MapBeanTreeModel;
import com.tp.baselib.zul.Qgrid;
import com.tp.baselib.zul.Tree;
import com.tp.baselib.zul.Treeitem;
import com.tp.baselib.zul.Window;
import com.tp.training_jhensin.yang.dao.StyleDAO;
import com.tp.training_jhensin.yang.dao.TPDAOFactory;
import com.tp.training_jhensin.yang.zul.TrainingBaseComposer;
import com.tp.training_jhensin.yang.zul.TrainingGeneralTreeAndEgridActionHandler;

public class StyleTreeController extends TrainingBaseComposer {
	@Wire
	Egrid masterGrid;
	// @Wire
	// Combobox brandNoCombo;
	@Wire("#queryWin #qgrid")
	private Qgrid qgrid;
	@Wire
	Tree indexTree;
	@Wire
	Combobox brandNoCombo;
	@Wire
	Combobox seasonNoCombo;
	@Wire
	Combobox styleYearCombo;

	private String qBrandId;
	private String qStyleNo;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		this.masterGrid.setActionHandler(new MasterActionHandler());

		super.doQuery();

		MapBean data = TPDAOFactory.getStyleDAO().getOneRow();
		this.masterGrid.setBean(data);

		MapBeanResultList brandNameData = TPDAOFactory.getWtBrandDAO().getBrandIdAndName();
		((Combobox) this.qgrid.getChildByFld("BRAND_ID")).setModel(new ListModelList<>(brandNameData));
		this.brandNoCombo.setModel(new ListModelList<>(brandNameData));
		this.addYearItem(2, this.styleYearCombo);
	}

	public void addYearItem(int yearRange, Combobox comboboxItem) {
		for (int i = 0; i < yearRange; i++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, i);
			Date date = cal.getTime();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy");
			String styleYear = ft.format(date);
			comboboxItem.appendItem(styleYear).setValue(styleYear);
		}
	}

	@Override
	protected boolean onQuery(MapBean bean) throws Exception {
		this.qBrandId = bean.get("BRAND_ID");
		this.qStyleNo = bean.get("STYLE_NO");

		TreeDataCtrler ctrler = new TreeDataCtrler();
		DynamicTreeNode<MapBean> rootNode = new DynamicTreeNode<>(ctrler);
		MapBeanTreeModel model = new MapBeanTreeModel(rootNode, true);
		this.indexTree.setModel(model);
		return true;
	}

	private class TreeDataCtrler implements DynamicTreeNode.DataCtrler<MapBean> {
		public TreeDataCtrler() {
			super();
		}

		@Override
		public List<MapBean> getChildrenData(DynamicTreeNode<MapBean> parentNode) throws Exception {
			StyleTreeController composer = StyleTreeController.this;
			MapBean parentData = parentNode.getData();
			int parentLevel = parentNode.getLevel(); // (0-base)
			StyleDAO dao = TPDAOFactory.getStyleDAO();
			if (parentLevel == -1) {
				return dao.queryLevel0(composer.qBrandId);
			} else if (parentLevel == 0) {
				return dao.queryLevel1((String) parentData.get("BRAND_ID"), composer.qStyleNo);
			} else if (parentLevel == 1) {
				return dao.queryLevel2((String) parentData.get("BRAND_ID"), (String) parentData.get("SEASON"),
						composer.qStyleNo);
			}
			return null;
		}

		@Override
		public int getChildCount(DynamicTreeNode<MapBean> parentNode) throws Exception {
			return -1;
		}
	}

	@Listen("onItemFocus=#indexTree")
	public void onIndexItemFocus() throws Exception {
		Treeitem focusedItem = this.indexTree.getFocusedItem();
		MapBean indexBean = DataContainer.Helper.getFocusedBean(this.indexTree);
		if (indexBean.isNew() || focusedItem.getLevel() == 2) {
			String currBrandId = (String) this.brandNoCombo.getFldValue();
			String brandId = indexBean.get("BRAND_ID");
			if (!ObjectUtils.equals(currBrandId, brandId)) {
				this.loadSeasonComboData(brandId);
			}
			this.masterGrid.setBean(indexBean);
		} else {
			this.masterGrid.setBean(null);
		}
	}

	public void loadSeasonComboData(String brandId) throws Exception {
		MapBeanResultList comboBrandNoBean = TPDAOFactory.getWtBrandSeasonDao()
				.getSeasonAndDescriptionByBrandId(brandId);
		this.seasonNoCombo.setModel(new ListModelList<>(comboBrandNoBean));
	}

	@Listen("onSelect=#brandNoCombo")
	public void brandNoComboItemFocus() throws Exception {
		Comboitem brandNoCombo = this.brandNoCombo.getSelectedItem();
		String currBrandId = (String) this.brandNoCombo.getFldValue();
		MapBeanResultList comboBrandNoBean = TPDAOFactory.getWtBrandSeasonDao()
				.getSeasonAndDescriptionByBrandId(currBrandId);
		this.seasonNoCombo.setModel(new ListModelList<>(comboBrandNoBean));
	}

	private class MasterActionHandler extends TrainingGeneralTreeAndEgridActionHandler {

		public MasterActionHandler() {
			super(TPDAOFactory.getStyleDAO(), false);
		}

		@Override
		protected void initNewBean(MapBean bean, MapBean parentBean, int sortNumber, EditEvent event)
				throws SQLException {
			if (parentBean != null) {
				String brandId = parentBean.get("BRAND_ID");
				String year = parentBean.get("#STYLE_YEAR");
				String seasonNo = parentBean.get("#SEASON_NO");

				bean.put("BRAND_ID", brandId);
				bean.put("STYLE_YEAR", year);
				bean.put("SEASON_NO", seasonNo);
			}
		}

		@Override
		public void onAfterSave(EditEvent event) throws Exception {
			MapBean bean = this.getEgrid().getBean();
			bean.put("TREE_LABEL", (String) bean.get("STYLE_NO")); // 對於 SQL 虛擬欄，需要自行更新
			Tree tree = (Tree) this.getEgrid().getBoundIndex();
			Treeitem focusItem = tree.getFocusedItem();
			Treeitem parentItem = (Treeitem) focusItem.getParentItem();
			String brandId = bean.get("BRAND_ID");
			String year = bean.get("STYLE_YEAR");
			String seasonNo = bean.get("SEASON_NO");
			if (parentItem != null && parentItem.getLevel() == 1) {
				MapBean parentBean = parentItem.getBean();
				if (ObjectUtils.equals(parentBean.get("BRAND_ID"), brandId)
						&& ObjectUtils.equals(parentBean.get("STYLE_YEAR"), year)
						&& ObjectUtils.equals(parentBean.get("SEASON_NO"), seasonNo)) {
					super.onAfterSave(event);
					return;
				}
			}
			// 若不在正確的位置，則需要另外處理
			DynamicTreeNode<MapBean> node = focusItem.getValue();
			StyleTreeController.this.focusItem(node);
		}
	}

	private void focusItem(DynamicTreeNode<MapBean> focusNode) throws Exception {
		MapBean focusBean = focusNode.getData();
		String brandId = focusBean.get("BRAND_ID");
		String year = focusBean.get("STYLE_YEAR");
		String seasonNo = focusBean.get("SEASON_NO");
		StyleDAO dao = TPDAOFactory.getStyleDAO();
		MapBeanTreeModel model = (MapBeanTreeModel) this.indexTree.getModel();
		if(brandId == null) {
			brandId = "Empty";
		}
		MapBean brandIdBean = dao.queryLevel0(brandId).get(0);
		DynamicTreeNode<MapBean> brandIdNode = (DynamicTreeNode<MapBean>) model.getNodeByData(model.getRoot(),
				brandIdBean);
		if (brandIdNode == null) {
			// 當現有節點中，沒有此第一層節點時，就加入
			brandIdNode = new DynamicTreeNode<MapBean>(brandIdBean);
			brandIdNode.setLoaded(); // 不要用 TreeDataCtrler 載資料
			model.getRoot().add(brandIdNode);
		}
		MapBean seasonBean = dao.getStyleData(brandId, year, seasonNo);
		DynamicTreeNode<MapBean> seasonNode = (DynamicTreeNode<MapBean>) model.getNodeByData(brandIdNode, seasonBean);
		if (seasonNode == null) {
			// 當現有節點中，沒有此第二層節點時，就加入
			seasonNode = new DynamicTreeNode<MapBean>(seasonBean);
			seasonNode.setLoaded();
			brandIdNode.add(seasonNode);
		}
		seasonNode.add(focusNode);
		this.indexTree.setFocusedItemByNode(focusNode, true);
		// 這裡會強制 render 上下層、並展開到該節點 (如果存在的話)
	}
}
