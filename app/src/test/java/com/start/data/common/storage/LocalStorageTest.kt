package com.start.data.common.storage

import com.start.data.common.cashe.CachePolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*

internal class LocalStorageTest {

    @Test
    fun getFromNetworkFirstTime(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn true
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock
        )

        /* When */
        val list = localStorage.get(key).take(1).toList()

        /* Then */
        Assert.assertEquals("13", list[0])
        verify(networkMock, times(1)).invoke(key)
    }

    @Test
    fun getFromNetworkIfCacheExpired(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn true
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock
        )
        /* When */
        val list1 = localStorage.get(key).take(1).toList()
        whenever(networkMock.invoke(any())).thenReturn("15")
        val list2 = localStorage.get(key).take(2).toList()
        /* Then */
        Assert.assertEquals("13", list1[0])
        Assert.assertEquals("13", list2[0])
        Assert.assertEquals("15", list2[1])
        verify(networkMock, times(2)).invoke(key)
    }

    @Test
    fun getFromNetworkIfMaxCache(): Unit = runBlocking {
        /* Given */
        val key1 = "1"
        val key2 = "2"
        val key3 = "3"
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(key1) } doReturn "100"
            on { invoke(key2) } doReturn "200"
            on { invoke(key3) } doReturn "300"
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 2,
            cachePolicy = cacheParams,
            network = networkMock
        )
        /* When */
        val listKey1 = localStorage.get(key1).take(1).toList()
        val listKey1New = localStorage.get(key1).take(1).toList()
        val listKey2 = localStorage.get(key2).take(1).toList()
        val listKey2New = localStorage.get(key2).take(1).toList()
        val listKey3 = localStorage.get(key3).take(1).toList()
        val listKey3New = localStorage.get(key3).take(1).toList()
        val listWithClean = localStorage.get(key1).take(1).toList()
        val listWithCleanNew = localStorage.get(key1).take(1).toList()
        /* Then */
        Assert.assertEquals("100", listKey1[0])
        Assert.assertEquals("100", listKey1New[0])
        Assert.assertEquals("200", listKey2[0])
        Assert.assertEquals("200", listKey2New[0])
        Assert.assertEquals("300", listKey3[0])
        Assert.assertEquals("300", listKey3New[0])
        Assert.assertEquals("100", listWithClean[0])
        Assert.assertEquals("100", listWithCleanNew[0])
        verify(networkMock, times(2)).invoke(key1)
        verify(networkMock, times(1)).invoke(key2)
        verify(networkMock, times(1)).invoke(key3)
    }

    @Test
    fun getFromCacheIfCacheNotExpired(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock
        )
        /* When */
        val list1 = localStorage.get(key).take(1).toList()
        whenever(networkMock.invoke(any())).thenReturn("15")
        val list2 = localStorage.get(key).take(1).toList()
        /* Then */
        Assert.assertEquals("13", list1[0])
        Assert.assertEquals("13", list2[0])
        verify(networkMock, times(1)).invoke(key)
    }

    @Test
    fun getFromNetworkWithoutDbData(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val dataBaseMock = mock<LocalStorage.DataBase<String, String>> {
            onBlocking { read(key) } doReturn null
            onBlocking { remove(key) } doReturn Unit
            onBlocking { insertOrUpdate(any(), any()) } doReturn Unit
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock,
            dataBase = dataBaseMock
        )
        /* When */
        val list1 = localStorage.get(key).take(1).toList()
        /* Then */
        Assert.assertEquals("13", list1[0])
        verify(networkMock, times(1)).invoke(key)
        verify(dataBaseMock, times(1)).read(key)
        verify(dataBaseMock, times(1)).insertOrUpdate(key, "13")
    }

    @Test
    fun getFromDbAndNetwork(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val dataBaseMock = mock<LocalStorage.DataBase<String, String>> {
            onBlocking { read(key) } doReturn "100"
            onBlocking { remove(key) } doReturn Unit
            onBlocking { insertOrUpdate(any(), any()) } doReturn Unit
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock,
            dataBase = dataBaseMock
        )
        /* When */
        val list1 = localStorage.get(key).take(2).toList()
        /* Then */
        Assert.assertEquals("100", list1[0])
        Assert.assertEquals("13", list1[1])
        verify(networkMock, times(1)).invoke(key)
        verify(dataBaseMock, times(1)).read(key)
        verify(dataBaseMock, times(1)).insertOrUpdate(key, "13")
    }

    @Test
    fun getFromDbAndNetworkWhenCacheExpired(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn true
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val dataBaseMock = mock<LocalStorage.DataBase<String, String>> {
            onBlocking { read(key) } doReturn "100"
            onBlocking { remove(key) } doReturn Unit
            onBlocking { insertOrUpdate(any(), any()) } doReturn Unit
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock,
            dataBase = dataBaseMock
        )
        /* When */
        val list1 = localStorage.get(key).take(2).toList()
        whenever(networkMock.invoke(any())).thenReturn("15")
        val list2 = localStorage.get(key).take(3).toList()
        /* Then */
        Assert.assertEquals("100", list1[0])
        Assert.assertEquals("13", list1[1])
        Assert.assertEquals("13", list2[0])
        Assert.assertEquals("15", list2[1])
        Assert.assertEquals("15", list2[2])
        verify(networkMock, times(3)).invoke(key)
        verify(dataBaseMock, times(1)).read(key)
        verify(dataBaseMock, times(1)).insertOrUpdate(key, "13")
        verify(dataBaseMock, times(2)).insertOrUpdate(key, "15")
    }

    @Test
    fun getFromCacheWhenCacheNotExpired(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val dataBaseMock = mock<LocalStorage.DataBase<String, String>> {
            onBlocking { read(key) } doReturn "100"
            onBlocking { remove(key) } doReturn Unit
            onBlocking { insertOrUpdate(any(), any()) } doReturn Unit
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock,
            dataBase = dataBaseMock
        )
        val list1 = localStorage.get(key).take(2).toList()
        Mockito.reset(networkMock)
        Mockito.reset(dataBaseMock)
        /* When */
        val list2 = localStorage.get(key).take(1).toList()
        /* Then */
        Assert.assertEquals("100", list1[0])
        Assert.assertEquals("13", list1[1])
        Assert.assertEquals("13", list2[0])
        verify(networkMock, times(0)).invoke(key)
        verify(dataBaseMock, times(0)).read(key)
        verify(dataBaseMock, times(0)).insertOrUpdate(key, "13")
    }

    @Test
    fun updateValue(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val mutex = Mutex()
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock
        )
        /* When */
        val list1 = localStorage.get(key).take(1).toList()
        val list2 = mutableListOf<String>()

        val differ = CoroutineScope(Dispatchers.Default).async {
            localStorage.get(key)
                .take(3)
                .collect {
                    list2.add(it)
                    if (mutex.isLocked) mutex.unlock()
                }
        }
        mutex.lock()
        mutex.withLock {
            localStorage.update(key) {
                "16"
            }
        }
        mutex.lock()
        mutex.withLock {
            localStorage.update(key) {
                "18"
            }
        }
        differ.await()
        /* Then */
        Assert.assertEquals("13", list1[0])
        Assert.assertEquals("13", list2[0])
        Assert.assertEquals("16", list2[1])
        Assert.assertEquals("18", list2[2])
    }

    @Test
    fun refresh(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val mutex = Mutex()
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock
        )

        /* When */
        val list = mutableListOf<String>()
        val differ = CoroutineScope(Dispatchers.Default).async {
            localStorage.get(key)
                .take(2)
                .collect {
                    list.add(it)
                    if (mutex.isLocked) mutex.unlock()
                }
        }
        mutex.lock()
        mutex.withLock {
            whenever(networkMock.invoke(any())).thenReturn("15")
            localStorage.refresh(key)
        }
        differ.await()
        /* Then */
        Assert.assertEquals("13", list[0])
        Assert.assertEquals("15", list[1])
        verify(networkMock, times(2)).invoke(key)
    }

    @Test
    fun cleanMemory(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val mutex = Mutex()
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock
        )

        /* When */
        val list = mutableListOf<String>()
        val differ = CoroutineScope(Dispatchers.Default).async {
            localStorage.get(key)
                .take(2)
                .collect {
                    list.add(it)
                    if (mutex.isLocked) mutex.unlock()
                }
        }
        mutex.lock()
        mutex.withLock {
            whenever(networkMock.invoke(any())).thenReturn("15")
            localStorage.clean()
        }
        differ.await()
        /* Then */
        Assert.assertEquals("13", list[0])
        Assert.assertEquals("15", list[1])
        verify(networkMock, times(2)).invoke(key)
    }

    @Test
    fun cleanMemoryAndDb(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val mutex = Mutex()
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val dataBaseMock = mock<LocalStorage.DataBase<String, String>> {
            onBlocking { read(key) } doReturn "100"
            onBlocking { remove(key) } doReturn Unit
            onBlocking { insertOrUpdate(any(), any()) } doReturn Unit
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock,
            dataBase = dataBaseMock
        )
        /* When */
        val list = mutableListOf<String>()
        val differ = CoroutineScope(Dispatchers.Default).async {
            localStorage.get(key)
                .take(3)
                .collect {
                    list.add(it)
                    if (mutex.isLocked && list.size >= 2) mutex.unlock()
                }
        }
        mutex.lock()
        mutex.withLock {
            whenever(dataBaseMock.read(any())).thenReturn(null)
            whenever(networkMock.invoke(any())).thenReturn("15")
            localStorage.clean()
        }
        differ.await()
        /* Then */
        Assert.assertEquals("100", list[0])
        Assert.assertEquals("13", list[1])
        Assert.assertEquals("15", list[2])
        verify(networkMock, times(2)).invoke(key)
        verify(dataBaseMock, times(2)).read(key)
        verify(dataBaseMock, times(1)).insertOrUpdate(key, "13")
        verify(dataBaseMock, times(1)).insertOrUpdate(key, "15")
        verify(dataBaseMock, times(1)).remove(key)
    }

    @Test
    fun cleanMemoryByKey(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val mutex = Mutex()
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock
        )

        /* When */
        val list = mutableListOf<String>()
        val differ = CoroutineScope(Dispatchers.Default).async {
            localStorage.get(key)
                .take(2)
                .collect {
                    list.add(it)
                    if (mutex.isLocked) mutex.unlock()
                }
        }
        mutex.lock()
        mutex.withLock {
            whenever(networkMock.invoke(any())).thenReturn("15")
            localStorage.clean(key)
        }
        differ.await()
        /* Then */
        Assert.assertEquals("13", list[0])
        Assert.assertEquals("15", list[1])
        verify(networkMock, times(2)).invoke(key)
    }

    @Test
    fun cleanMemoryAndDbByKey(): Unit = runBlocking {
        /* Given */
        val key = "1"
        val mutex = Mutex()
        val cacheParams = mock<CachePolicy> {
            on { isExpired(any()) } doReturn false
        }
        val networkMock = mock<(String) -> String> {
            on { invoke(any()) } doReturn "13"
        }
        val dataBaseMock = mock<LocalStorage.DataBase<String, String>> {
            onBlocking { read(key) } doReturn "100"
            onBlocking { remove(key) } doReturn Unit
            onBlocking { insertOrUpdate(any(), any()) } doReturn Unit
        }
        val localStorage: LocalStorage<String, String> = LocalStorage(
            maxElements = 1,
            cachePolicy = cacheParams,
            network = networkMock,
            dataBase = dataBaseMock
        )
        /* When */
        val list = mutableListOf<String>()
        val differ = CoroutineScope(Dispatchers.Default).async {
            localStorage.get(key)
                .take(3)
                .collect {
                    list.add(it)
                    if (mutex.isLocked && list.size >= 2) mutex.unlock()
                }
        }
        mutex.lock()
        mutex.withLock {
            whenever(dataBaseMock.read(any())).thenReturn(null)
            whenever(networkMock.invoke(any())).thenReturn("15")
            localStorage.clean(key)
        }
        differ.await()
        /* Then */
        Assert.assertEquals("100", list[0])
        Assert.assertEquals("13", list[1])
        Assert.assertEquals("15", list[2])
        verify(networkMock, times(2)).invoke(key)
        verify(dataBaseMock, times(2)).read(key)
        verify(dataBaseMock, times(1)).insertOrUpdate(key, "13")
        verify(dataBaseMock, times(1)).insertOrUpdate(key, "15")
        verify(dataBaseMock, times(1)).remove(key)
    }

}