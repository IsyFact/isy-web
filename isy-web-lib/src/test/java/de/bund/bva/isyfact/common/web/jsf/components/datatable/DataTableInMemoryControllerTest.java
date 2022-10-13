package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataTableInMemoryControllerTest {

    @Mock
    private DataTableInMemoryModel<DataTableItem> model;

    @Mock
    private DataTablePaginationModel paginationModel;

    @Mock
    private DataTableInMemoryDataModel<DataTableItem> inMemoryDataModel;

    @Mock
    private DataTableSortModel sortModel;

    private DataTableInMemoryController<DataTableItem, DataTableInMemoryModel<DataTableItem>> controller;

    private List<DataTableItem> testItems;

    @Before
    public void setup() {
        reset(model, inMemoryDataModel);

        when(model.getPaginationModel()).thenReturn(paginationModel);
        when(model.getDataModel()).thenReturn(inMemoryDataModel);
        when(model.getSortModel()).thenReturn(sortModel);
        when(sortModel.getProperty()).thenReturn("");

        controller = new DataTableInMemoryController<>();

        testItems = new ArrayList<>();

        testItems.add(new TestDataTableItem(2));
        testItems.add(new TestDataTableItem(0));
        testItems.add(new TestDataTableItem(1));
    }

    @Test
    public void filterDisplayItemsEmptyFilter() {
        List<DataTableItem> result;
        Map<String, String> filter = new HashMap<>();

        result = controller.filterDisplayItems(testItems, null);
        assertEquals(testItems, result);

        result = controller.filterDisplayItems(testItems, filter);
        assertEquals(testItems, result);

        filter.put("id", "");
        result = controller.filterDisplayItems(testItems, filter);
        assertEquals(testItems, result);

        filter.put("", "5");
        result = controller.filterDisplayItems(testItems, filter);
        assertEquals(testItems, result);

        filter.put("id", null);
        result = controller.filterDisplayItems(testItems, filter);
        assertEquals(testItems, result);

        filter.put(null, "5");
        result = controller.filterDisplayItems(testItems, filter);
        assertEquals(testItems, result);
    }

    @Test
    public void filterDisplayItems() {
        Map<String, String> filter = new HashMap<>();
        filter.put("id", "2");

        List<DataTableItem> result = controller.filterDisplayItems(testItems, filter);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getIdentifierForItem());
    }

    @Test
    public void getItemById() {
        when(inMemoryDataModel.getAllItems()).thenReturn(testItems);

        DataTableItem item = controller.getItemById(model, 1);

        assertNotNull(item);
        assertEquals(1, item.getIdentifierForItem());
        assertNull(controller.getItemById(model, -1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void setItems() {
        controller = spy(DataTableInMemoryController.class);

        controller.setItems(model, testItems);

        verify(model).setAllitems(testItems);
        verify(controller).invalidateModel(model);
        verify(controller).updateDisplayItems(model);
    }

    @Test
    public void invalidateModel() {
        when(model.getMode()).thenReturn(DataTableModel.DatatableOperationMode.SERVER);

        controller.invalidateModel(model);

        verify(inMemoryDataModel).setAllItems(null);
        verify(model).setSelectionModel(any(DataTableSelectionModel.class));
        verify(paginationModel).setCurrentPage(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void emptyTable() {
        controller = spy(DataTableInMemoryController.class);

        controller.emptyTable(model);

        verify(inMemoryDataModel).setAllItems(Collections.emptyList());
        verify(model).setSelectionModel(any(DataTableSelectionModel.class));
        verify(paginationModel).setCurrentPage(1);

        verify(controller).updateDisplayItems(model);
    }

    @Test
    public void paginateItems() {
        DataTableRequest request = mock(DataTableRequest.class);
        when(request.getItemFrom()).thenReturn(0);
        when(request.getItemsCount()).thenReturn(Integer.MAX_VALUE);

        List<DataTableItem> result = controller.paginateItems(testItems, request);

        assertEquals(testItems, result);

        when(request.getItemFrom()).thenReturn(1);
        when(request.getItemsCount()).thenReturn(1);

        result = controller.paginateItems(testItems, request);

        assertEquals(testItems.subList(1, 2), result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readItemsClientModus() {
        List<DataTableItem> items = mock(ArrayList.class);
        when(items.size()).thenReturn(100);
        when(inMemoryDataModel.getAllItems()).thenReturn(items);
        when(model.getMode()).thenReturn(DataTableModel.DatatableOperationMode.CLIENT);

        DataTableResult<DataTableItem> result = controller.readItems(model, null);

        assertEquals(100, result.getItemCount());
        assertEquals(items, result.getItems());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readItemsServerModus() {
        when(inMemoryDataModel.getAllItems()).thenReturn(testItems);
        when(model.getMode()).thenReturn(DataTableModel.DatatableOperationMode.SERVER);

        controller = spy(DataTableInMemoryController.class);

        DataTableRequest request = mock(DataTableRequest.class);
        when(request.getItemsCount()).thenReturn(testItems.size());
        when(request.getItemFrom()).thenReturn(0);

        DataTableResult<DataTableItem> result = controller.readItems(model, request);

        verify(request).getFilter();
        verify(controller).filterDisplayItems(eq(testItems), anyMap());

        verify(request).getSortDirection();
        verify(request).getSortProperty();
        verify(controller).sortDisplayItems(testItems, request.getSortProperty(), request.getSortDirection());

        verify(controller).paginateItems(testItems, request);

        assertEquals(testItems, result.getItems());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getAllItems() {
        List<DataTableItem> allItems = mock(ArrayList.class);
        when(model.getAllitems()).thenReturn(allItems);

        List<DataTableItem> result = controller.getAllItems(model);

        assertEquals(allItems, result);
    }

    @Test
    public void sortDisplayItems() {
        controller.sortDisplayItems(testItems, "id", SortDirection.ASCENDING);

        for (int i = 0; i < testItems.size(); i++) {
            assertEquals(i, testItems.get(i).getIdentifierForItem());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void sortDisplayItemsEmptySortProperty() {
        List<DataTableItem> items = mock(List.class);

        controller.sortDisplayItems(items, "", SortDirection.ASCENDING);

        verifyNoInteractions(items);
    }

    public static class TestDataTableItem implements DataTableItem {

        long id;

        public TestDataTableItem(long id) {
            this.id = id;
        }

        @Override
        public long getIdentifierForItem() {
            return id;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}