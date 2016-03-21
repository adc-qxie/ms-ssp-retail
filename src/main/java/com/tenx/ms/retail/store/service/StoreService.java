package com.tenx.ms.retail.store.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tenx.ms.commons.rest.dto.Paginated;
import com.tenx.ms.retail.store.domain.StoreEntity;
import com.tenx.ms.retail.store.repository.StoreRepository;
import com.tenx.ms.retail.store.rest.dto.CreateStore;
import com.tenx.ms.retail.store.rest.dto.Store;

@Service
public class StoreService {

	@Autowired
	private StoreRepository storeRepository;

	public Optional<Store> getStoreByID(Long storeId) {
		return storeRepository.findOneByStoreId(storeId).map(store -> convertToDTO(store));
	}

	public Paginated<Store> getStoreByName(Pageable pageable, String baseLinkPath, String storeName) {
		Page<StoreEntity> page = storeRepository.findByStoreName(storeName, pageable);

		List<Store> stores = page.getContent().stream().map(store -> convertToDTO(store)).collect(Collectors.toList());

		return Paginated.wrap(page, stores, baseLinkPath);
	}

	/**
	 * Retrieves All Stores
	 * 
	 * @param pageable
	 *            the pageable options used for paginated results
	 * @param baseLinkPath
	 *            the base path to the resource used for pagination link generation
	 * @return Paginated Iterable of Stores
	 */
	public Paginated<Store> getAllStores(Pageable pageable, String baseLinkPath) {
		Page<StoreEntity> page = storeRepository.findAll(pageable);

		List<Store> stores = page.getContent().stream().map(store -> convertToDTO(store)).collect(Collectors.toList());

		return Paginated.wrap(page, stores, baseLinkPath);
	}

	public Store createStore(CreateStore store) {
		if (getStoreByName(null, null, store.getStoreName()).getTotalCount() > 0) {
			return null;
		}

		return convertToDTO(storeRepository.save(convertFromDTO(store)));
	}

	private Store convertToDTO(StoreEntity store) {
		Store s = new Store();
		s.setStoreId(store.getStoreId());
		s.setStoreName(store.getStoreName());
		return s;
	}

	private StoreEntity convertFromDTO(CreateStore store) {
		StoreEntity s = new StoreEntity();
		s.setStoreName(store.getStoreName());
		return s;
	}
}
