<!-- markdownlint-disable no-duplicate-heading no-trailing-punctuation -->

# DEPRECATED

# Operation documentation is now embedded in the swagger documentation as of 2.0.1

# Rest Gateway Operations

* [ADD](#add)
* [APPEND](#append)
* [GET](#get)
* [PREPEND](#prepend)
* [READ](#read)
* [GET_HEADER](#get_header)
* [TOUCH](#touch)
* [PUT](#put)

## List Operations

* [LIST_APPEND](#list_append)
* [LIST_APPEND_ITEMS](#list_append_items)
* [LIST_CLEAR](#list_clear)
* [LIST_GET](#list_get)
* [LIST_GET_BY_INDEX](#list_get_by_index)
* [LIST_GET_BY_INDEX_RANGE](#list_get_by_index_range)
* [LIST_GET_BY_RANK](#list_get_by_rank)
* [LIST_GET_BY_RANK_RANGE](#list_get_by_rank_range)
* [LIST_GET_BY_VALUE](#list_get_by_value)
* [LIST_GET_BY_VALUE_RANGE](#list_get_by_value_range)
* [LIST_GET_BY_VALUE_LIST](#list_get_by_value_list)
* [LIST_GET_RANGE](#list_get_range)
* [LIST_INCREMENT](#list_increment)
* [LIST_INSERT](#list_insert)
* [LIST_INSERT_ITEMS](#list_insert_items)
* [LIST_POP](#list_pop)
* [LIST_POP_RANGE](#list_pop_range)
* [LIST_REMOVE](#list_remove)
* [LIST_REMOVE_BY_INDEX](#list_remove_by_index)
* [LIST_REMOVE_BY_INDEX_RANGE](#list_remove_by_index_range)
* [LIST_REMOVE_BY_RANK](#list_remove_by_rank)
* [LIST_REMOVE_BY_RANK_RANGE](#list_remove_by_rank_range)
* [LIST_REMOVE_BY_VALUE](#list_remove_by_value)
* [LIST_REMOVE_BY_VALUE_RANGE](#list_remove_by_value_range)
* [LIST_REMOVE_BY_VALUE_LIST](#list_remove_by_value_list)
* [LIST_REMOVE_RANGE](#list_remove_range)
* [LIST_SET](#list_set)
* [LIST_SET_ORDER](#list_set_order)
* [LIST_SIZE](#list_size)
* [LIST_SORT](#list_sort)
* [LIST_TRIM](#list_trim)

## Map Operations

* [MAP_CLEAR](#map_clear)
* [MAP_DECREMENT](#map_decrement)
* [MAP_GET_BY_INDEX](#map_get_by_index)
* [MAP_GET_BY_INDEX_RANGE](#map_get_by_index_range)
* [MAP_GET_BY_KEY](#map_get_by_key)
* [MAP_GET_BY_KEY_LIST](#map_get_by_key_list)
* [MAP_GET_BY_KEY_RANGE](#map_get_by_key_range)
* [MAP_GET_BY_RANK](#map_get_by_rank)
* [MAP_GET_BY_RANK_RANGE](#map_get_by_rank_range)
* [MAP_GET_BY_VALUE](#map_get_by_value)
* [MAP_GET_BY_VALUE_RANGE](#map_get_by_value_range)
* [MAP_GET_BY_VALUE_LIST](#map_get_by_value_list)
* [MAP_INCREMENT](#map_increment)
* [MAP_PUT](#map_put)
* [MAP_PUT_ITEMS](#map_put_items)
* [MAP_REMOVE_BY_INDEX](#map_remove_by_index)
* [MAP_REMOVE_BY_INDEX_RANGE](#map_remove_by_index_range)
* [MAP_REMOVE_BY_KEY](#map_remove_by_key)
* [MAP_REMOVE_BY_KEY_RANGE](#map_remove_by_key_range)
* [MAP_REMOVE_BY_RANK](#map_remove_by_rank)
* [MAP_REMOVE_BY_RANK_RANGE](#map_remove_by_rank_range)
* [MAP_REMOVE_BY_VALUE](#map_remove_by_value)
* [MAP_REMOVE_BY_VALUE_RANGE](#map_remove_by_value_range)
* [MAP_REMOVE_BY_VALUE_LIST](#map_remove_by_value_list)
* [MAP_SET_POLICY](#map_set_policy)
* [MAP_SIZE](#map_size)

## ADD

Increment the value of an item in the specified `bin` by the value of `incr`

### Required fields

* `bin`
* `incr`

### Optional fields:

* None

### Example

```javascript
{
    "operation": "ADD",
    "opValues": {
        "bin": "bin_name",
        "incr": 5
    }
}
/*
If the stored record was:
{
    "bin_name": 5
}
After this operation, it will be
{
    "bin_name": 10
}
*/
```

## APPEND

Append a `value` to the item in the specified `bin`

### Required fields

* `bin`
* `value`

### Optional Fields:

* None

### Example:

```javascript
{
    "operation": "APPEND",
    "opValues": {
        "bin": "bin_name",
        "value": "spike"
    }
}
/*
If the stored record was:
{
    "bin_name": "aero"
}
After this operation it will be
{
    "bin_name": "aerospike"
}
*/
```

## GET

Return the contents of a record.

### Required Fields:

* None

### Optional Fields:

* None

### Example:

```javascript
{
    "operation": "GET",
    "opValues": {}
}

/*
If the stored record was:
{
    "bin_name": "aerospike",
    "other_bin": "other_val"
}
this operation will return an object such as:
{
    "bins": {
        "other_bin": "other_val",
        "bin_name": "aerospike"
    },
    "generation": 3,
    "ttl": 2591967
}
*/
```

## PREPEND

Prepend an `value` to the item in the specified `bin`.

### Required fields

* `bin`
* `value`

### Optional Fields:

* None

### Example:

```javascript
{
    "operation": "PREPEND",
    "opValues": {
        "bin": "bin_name",
        "value": "aero"
    }
}
/*
If the stored record was:
{
    "bin_name": "spike"
}
after this operation it will be:
{
    "bin_name": "aerospike"
}
*/
```

## READ

Return the value of a specified `bin`.

### Required fields

* `bin`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "READ",
    "opValues": {
        "bin": "bin_name"
    }
}
/*
If the stored record was:
{
    "bin_name": "spike",
    "other_bin": "other_val"
}
This operation will return an object similar to:

{
    "bins": {
        "bin_name": "aerospike"
    },
    "generation": 3,
    "ttl": 2591919
}
*/
```

## GET_HEADER

Return metadata about a record.

### Required fields

* None

### Optional fields:

* None

```javascript
{
    "operation": "GET_HEADER",
    "opValues": {}
}
/*
This operation will return a record object including metadata, and without bins:
{
    "bins": null,
    "generation": 3,
    "ttl": 2591750
}
*/
```

## TOUCH

Reset a record’s TTL and increment its generation.

### Required fields

* None

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "TOUCH",
    "opValues": {}
}
/*
    If prior to this operation the generation of the record wass 3, after the operation
    it will be 4.
*/
```

## PUT

Store a `value` in the specified `bin`.

### Required fields

* `bin`
* `value`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "PUT",
    "opValues": {
        "bin": "data",
        "value": "base"
    }
}
/*
If the stored record was:
{
    "bin1": "aerospike"
}
after this operation it will be
{
    "bin1": "aerospike",
    "data": "base"
}
*/
```

## LIST_APPEND

Append a `value` to a list stored in the specified `bin`.

### Required fields

* `bin`
* `value`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_APPEND",
    "opValues": {
        "bin": "listbin",
        "value": "last_item"
    }
}
/*

If the stored record was
{
    "listbin": [1, 2, 1]
}

After this operation the value will be:
{
    "listbin": [1, 2, 1, "last_item"]
}
*/
```

## LIST_APPEND_ITEMS

Append multiple items to a list stored in the specified bin.

### Required fields

* `bin`
* `values`

### Optional fields:

* `listPolicy`

### Example:

```javascript
{
    "operation": "LIST_APPEND_ITEMS",
    "opValues": {
        "bin": "listbin",
        "values": [1, 2, 3]
    }
}

/* If the stored record was:
{
    "listbin": ["a", "b", "c"]
}
This operation will return an object containing the new length of the list:
{
    "bins": {
        "listbin": 6
    },
    "generation": 2,
    "ttl": 2592000
}
and the value of the stored record will be:
{
    "listbin": ["a", "b", "c" , 1, 2, 3]
}
*/

```

## LIST_CLEAR

Empty a list stored in the specified bin.

### Required fields

* `bin`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_CLEAR",
    "opValues": {
        "bin": "listbin"
    }
}
/*
If the stored record wass:
{
    "listbin": ["a", "b", "c"]
}
After this operation the record will be:
{
    "listbin": []
}
*/

```

## LIST_GET

Return an item, located a specific index, from a list in the specified bin.

### Required fields

* `bin`
* `index`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_GET",
    "opValues": {
        "bin": "listbin",
        "index": 1
    }
}
/*
If prior to the operation the stored record was:
{
    "listbin": ["a", "b", "c"]
}
this operation will return
{
    "bins": {
        "listbin": "b"
    },
    "generation": 3,
    "ttl": 2591965
}
```

## LIST_GET_BY_INDEX

Return an item, located a specific index, from a list in the specified bin. The value of `listReturnType` determines
what will be returned.
Requires Aerospike Server `3.16.0.1` or greater.

### Required fields

* `bin`
* `Index`
* `listReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_GET_BY_INDEX",
    "opValues": {
        "bin": "listbin",
        "index": 1,
        "listReturnType": "RANK"
    }
}
/*
If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2]
}
this operation will return
{
    "bins": {
        "listbin": 2
    },
    "generation": 4,
    "ttl": 2591922
}
```

## LIST_GET_BY_INDEX_RANGE

Return a specified amount of items beginning at a specific index, from a list in the specified bin. If `count` is not
provided, all items from `index` until the end of the list will be returned.

Requires Aerospike Server `3.16.0.1` or later

### Required fields

* `bin`
* `Index`
* `listReturnType`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "LIST_GET_BY_INDEX_RANGE",
    "opValues": {
        "bin": "listbin",
        "index": 1,
        "count": 2,
        "listReturnType": "VALUE"
    }
}
/*
If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2]
}
this operation will return
{
    "bins": {
        "listbin": [3, 1]
    },
    "generation": 4,
    "ttl": 2591922
}
```

## LIST_GET_BY_RANK

Return a list item with the specified `rank`.

Requires Aerospike Server `3.16.0.1` or later.

### Required fields

* `bin`
* `rank`
* `listReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_GET_BY_RANK",
    "opValues": {
        "bin": "listbin",
        "rank": -1,
        "listReturnType": "VALUE"
    }
}
/*
If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2]
}
this operation will return
{
    "bins": {
        "listbin": 5
    },
    "generation": 4,
    "ttl": 2591922
}
*/
```

## LIST_GET_BY_RANK_RANGE

Return `count` items beginning with the specified rank. If `count` is omitted, all items beginning with specified rank
will be returned.

Requires Aerospike Server `3.16.0.1` or later.

### Required fields

* `bin`
* `rank`
* `listReturnType`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "LIST_GET_BY_RANK_RANGE",
    "opValues": {
        "bin": "listbin",
        "rank": 2,
        "count": 2,
        "listReturnType": "VALUE"
    }
}
/*
If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2]
}
this operation will return
{
    "bins": {
        "listbin": [3, 4]
    },
    "generation": 4,
    "ttl": 2591922
}
*/
```

## LIST_GET_BY_VALUE

Return all items in a list with a value matching a specified value.

### Required fields

* `bin`
* `value`
* `listReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_GET_BY_VALUE",
    "opValues": {
        "bin": "listbin",
        "value": 5,
        "listReturnType": "COUNT"
    }
}
/*
If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return
{
    "bins": {
        "listbin": 2
    },
    "generation": 4,
    "ttl": 2591922
}
*/
```

## LIST_GET_BY_VALUE_RANGE

Return all items in a list with values between `valueBegin` and `valueEnd`. If `valueBegin` is omitted, all items with a
value less than `valueEnd` will be returned. If `valueEnd` is omitted, all items with a value greater than `valueBegin`
will be returned.

Requires Aerospike Server `3.16.0.1` or later.

### Required fields

* `bin`
* `listReturnType`

### Optional fields:

* `valueBegin`
* `valueEnd`

### Example:

```javascript
{
    "operation": "LIST_GET_BY_VALUE_RANGE",
    "opValues": {
        "bin": "listbin",
        "valueBegin": 2,
        "valueEnd": 5,
        "listReturnType": "VALUE"
    }
}
/*
If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return
{
    "bins": {
        "listbin": [3, 2, 4] // Note this ordering is not guaranteed
    },
    "generation": 4,
    "ttl": 2591922
}
*/
```

## LIST_GET_BY_VALUE_LIST

Return all items in a list with values that are contained in the specified list of values.

Requires Aerospike Server `3.16.0.1` or later.

### Required fields

* `bin`
* `listReturnType`
* `values`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_GET_BY_VALUE_LIST",
    "opValues": {
        "bin": "listbin",
        "values": [2, 5],
        "listReturnType": "COUNT"
    }
}
/*
If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return
{
    "bins": {
        "listbin": 3
    },
    "generation": 4,
    "ttl": 2591922
}
*/
```

## LIST_GET_RANGE

Get `count` items from the list beginning with the specified index. If `count` is omitted, all items from `index` to the
end of the list will be returned.

### Required fields

* `bin`
* `index`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "LIST_GET_RANGE",
    "opValues": {
        "bin": "listbin",
        "index": 1,
        "count": 3
    }
}
/*
If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return
{
    "bins": {
        "listbin": [3, 1, 4]
    },
    "generation": 4,
    "ttl": 2591922
}
*/
```

## LIST_INCREMENT

Increment the value of a an item of a list at the specified index, by the value of `incr`

### Required fields

* `bin`
* `index`

### Optional fields:

* `incr`
* `listPolicy`

### Example:

```javascript
{
    "operation": "LIST_INCREMENT",
    "opValues": {
        "bin": "listbin",
        "index": 1,
        "incr": 10
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return an object containing the new value of the incremented list entry
{
    "bins": {
        "listbin": 13
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [5, 13, 1, 4, 2, 5]
}
*/
```

## LIST_INSERT

Insert a value into a list at the specified index.

### Required fields

* `bin`
* `Index`
* `value`

### Optional fields:

* `listPolicy`

### Example:

```javascript
{
    "operation": "LIST_INSERT",
    "opValues": {
        "bin": "listbin",
        "index": 1,
        "value": "new_item"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return an object containing the new length of the list
{
    "bins": {
        "listbin": 7
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [5, "new_item", 3, 1, 4, 2, 5]
}
*/
```

## LIST_INSERT_ITEMS

Insert multiple items into a list at the specified `index`.

### Required fields

* `bin`
* `Index`
* `values`

### Optional fields:

* `listPolicy`

### Example:

```javascript
{
    "operation": "LIST_INSERT_ITEMS",
    "opValues": {
        "bin": "listbin",
        "index": 1,
        "values": ["item1", "item2"]
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return an object containing the new length of the list
{
    "bins": {
        "listbin": 8
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [5, "item1", "item2", 3, 1, 4, 2, 5]
}
*/
```

## LIST_POP

Remove and return a list value at the specified `index`.

### Required fields

* `bin`
* `index`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_POP",
    "opValues": {
        "bin": "listbin",
        "index": 1
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return an object containing the popped item:
{
    "bins": {
        "listbin": 3
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [5, 1, 4, 2, 5]
}
*/
```

## LIST_POP_RANGE

Remove and return `count` items beginning at the specified `index` from the list.
If `count` is omitted, all items beginning from `index` will be removed and returned.

### Required fields

* `bin`
* `index`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "LIST_POP_RANGE",
    "opValues": {
        "bin": "listbin",
        "index": 2,
        "count": 2
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return an object containing the popped items:
{
    "bins": {
        "listbin": [1, 4]
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [5, 3, 2, 5]
}
*/
```

## LIST_REMOVE

Remove a list item at the specified index.

### Required fields

* `bin`
* `index`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_REMOVE",
    "opValues": {
        "bin": "listbin",
        "index": 3
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return the number of items removed:
{
    "bins": {
        "listbin": 1
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [5, 3, 1, 2, 5]
}
*/
```

## LIST_REMOVE_BY_INDEX

Remove a list item at the specified index.

Requires Aerospike Server `3.16.0.1` or later.

### Required fields

* `bin`
* `index`
* `listReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_REMOVE_BY_INDEX",
    "opValues": {
        "bin": "listbin",
        "index": 3,
        "listReturnType": "VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return a value determined by listReturnType
{
    "bins": {
        "listbin": 4
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [5, 3, 1, 2, 5]
}
*/
```

## LIST_REMOVE_BY_INDEX_RANGE

Remove and return `count` items beginning at the specified `index` from the list.
If `count` is omitted, all items beginning from `index` will be removed and returned.

Requires Aerospike Server 3.16.0.1 or later

### Required fields

* `bin`
* `index`
* `listReturnType`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "LIST_REMOVE_BY_INDEX_RANGE",
    "opValues": {
        "bin": "listbin",
        "index": 0,
        "count": 2,
        "listReturnType": "VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return a value determined by listReturnType
{
    "bins": {
        "listbin": [5, 3]
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [1, 4, 2, 5]
}
*/
```

## LIST_REMOVE_BY_RANK

Remove a list item with the specified rank.
Requires Aerospike Server `3.16.0.1` or later

### Required fields

* `bin`
* `rank`
* `listReturnType`

### Optional fields:

* None

### Example:

```javascript

{
    "operation": "LIST_REMOVE_BY_RANK",
    "opValues": {
        "bin": "listbin",
        "rank": -1,
        "listReturnType": "INDEX"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return a value determined by listReturnType
{
    "bins": {
        "listbin": 0
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [3, 1, 4, 2, 5]
}
*/
```

## LIST_REMOVE_BY_RANK_RANGE

Remove `count` items from a list, beginning with the item with the specified `rank`. If `count` is omitted, all items
beginning with the specified `rank` will be removed and returned.

Requires Aerospike Server `3.16.0.1` or later.

### Required fields

* `bin`
* `rank`
* `listReturnType`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "LIST_REMOVE_BY_RANK_RANGE",
    "opValues": {
        "bin": "listbin",
        "rank": -4,
        "count": 2,
        "listReturnType": "VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return a value determined by listReturnType
{
    "bins": {
        "listbin": [4, 3]  // This orderning is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [5, 1, 2, 5]
}
*/
```

## LIST_REMOVE_BY_VALUE

Remove and return list entries with a value equal to the specified value.
Requires Aerospike Server `3.16.0.`1 or later

### Required fields

* `bin`
* `value`
* `listReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_REMOVE_BY_VALUE",
    "opValues": {
        "bin": "listbin",
        "value": 5,
        "listReturnType": "COUNT"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return a value determined by listReturnType
{
    "bins": {
        "listbin": 2
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [3, 1, 4, 2]
}
*/
```

## LIST_REMOVE_BY_VALUE_RANGE

Remove all items from the list with values between `valueBegin` and `valueEnd`. If `valueBegin` is omitted all items
with a value less than `valueEnd` will be removed. If `valueEnd` is omitted all items with a value greater
than `valueBegin` will be removed.

Requires Aerospike Server `3.16.0.1` or later.

### Required fields

* `bin`
* `listReturnType`

### Optional fields:

* `valueBegin`
* `valueEnd`

### Example:

```javascript
{
    "operation": "LIST_REMOVE_BY_VALUE_RANGE",
    "opValues": {
        "bin": "listbin",
        "valueBegin": 4,
        "valueEnd": 6,
        "listReturnType": "COUNT"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return a value determined by listReturnType
{
    "bins": {
        "listbin": 3
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [3, 1, 2]
}
*/
```

## LIST_REMOVE_BY_VALUE_LIST

Remove all items from the list with values contained in the specified list of values.

Requires Aerospike Server `3.16.0.1` or later

### Required fields

* `bin`
* `values`
* `listReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_REMOVE_BY_VALUE_LIST",
    "opValues": {
        "bin": "listbin",
        "values": [1, 4, 5],
        "listReturnType": "COUNT"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return a value determined by listReturnType
{
    "bins": {
        "listbin": 4
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [3, 2]
}
*/
```

## LIST_REMOVE_RANGE

Remove `count` items beginning at the specified `index` from the list.
If `count` is omitted, all items beginning from `index` will be removed and returned.

### Required fields

* `bin`
* `index`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "LIST_REMOVE_RANGE",
    "opValues": {
        "bin": "listbin",
        "index": 4,
        "count": 3
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return the number of items removed
{
    "bins": {
        "listbin": 2
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [5, 3, 1, 4]
}
*/
```

## LIST_SET

Set the value at the specified index to the specified value.

### Required fields

* `bin`
* `index`
* `value`

### Optional fields:

* `listPolicy`

### Example:

```javascript
{
    "operation": "LIST_SET",
    "opValues": {
        "bin": "listbin",
        "index": 1,
        "value": "aerospike",
        "listPolicy": {
            "order": "ORDERED",
            "writeFlags": ["ADD_UNIQUE"]
        }
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}

After the operation the value of the record will be
{
   "listbin": [5, "aerospike", 1, 4, 2, 5]
}
*/
```

## LIST_SET_ORDER

Set an ordering for the list.

### Required fields

* `bin`
* `listOrder`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_SET_ORDER",
    "opValues": {
        "bin": "list",
        "listOrder": "ORDERED"
    }
}
```

## LIST_SIZE

Return the size of the list.

### Required fields

* `bin`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_SIZE",
    "opValues": {
        "bin": "listbin"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}

this operation will return the size of the list
{
    "bins": {
        "listbin": 6
    },
    "generation": 1,
    "ttl": 2592000
}
*/
```

## LIST_SORT

Perform a sort operation on the list.
Requires Aerospike Server `3.16.0.1` or later.

### Required fields

* `bin`

### Optional fields:

* `list_sort_flags`

### Example:

```javascript
{
    "operation": "LIST_SORT",
    "opValues": {
        "bin": "listbin"
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}

After the operation the value of the record will be
{
   "listbin": [1, 2, 3, 4, 5, 5]
}
*/
```

## LIST_TRIM

Trim the list to the specified range. Items with indexes in the range `[index, index + count)` will be retained.

### Required fields

* `bin`
* `index`
* `count`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "LIST_TRIM",
    "opValues": {
        "bin": "listbin",
        "index": 1,
        "count": 4
    }
}
/*If prior to the operation the stored record was:
{
    "listbin": [5, 3, 1, 4, 2, 5]
}
this operation will return the number of removed items
{
    "bins": {
        "listbin": 2
    },
    "generation": 2,
    "ttl": 2592000
}
And the value of the record will be
{
   "listbin": [3, 1, 4, 2]
}
*/
```

## MAP_CLEAR

Empty the specified map.

### Required fields

* `bin`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_CLEAR",
    "opValues": {
        "bin": "mapbin"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}

After the operation value of the record will be
{
   "mapbin": {}
}
*/
```

## MAP_DECREMENT

Decrement the map item with the specified `key` by the amount specified by `decr`.

### Required fields

* `bin`
* `decr`
* `key`

### Optional fields:

* `mapPolicy` See [Map Policy](#Map-Policy)

### Example:

```javascript
{
    "operation": "MAP_DECREMENT",
    "opValues": {
        "bin": "mapbin",
        "key": "a",
        "decr": 10
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return the value of the decremented item
{
    "bins": {
        "mapbin": -9
    },
    "generation": 2,
    "ttl": 2592000
}
and the value of the record will be
{
   "mapbin": "mapbin": {"a": -9, "b":2, "c":0, "d":3}
}
*/
```

## MAP_GET_BY_INDEX

Get the map item at the specified `index`.

### Required fields

* `bin`
* `index`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_GET_BY_INDEX",
    "opValues": {
        "bin": "map",
        "index": 1,
        "mapReturnType": "KEY"
    }
}
```

## MAP_GET_BY_INDEX_RANGE

Get all map items with indexes in the range `[index, index + count)` . If `count` is omitted, all items beginning with
the item at the specified index will be returned.

### Required fields

* `bin`
* `index`
* `mapReturnType`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "MAP_GET_BY_INDEX_RANGE",
    "opValues": {
        "bin": "map",
        "index": 1,
        "count": 3,
        "mapReturnType": "KEY"
    }
}
```

## MAP_GET_BY_KEY

Return the value with the specified key from the map.

### Required fields

* `bin`
* `key`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_GET_BY_KEY",
    "opValues": {
        "bin": "mapbin",
        "key": "c",
        "mapReturnType": "VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the item associated with the specified key
{
    "bins": {
        "mapbin": 0
    },
    "generation": 2,
    "ttl": 2592000
}
*/
```

## MAP_GET_BY_KEY_LIST

Remove values with the specified keys from the map.

Requires Aerospike Server `3.16.0.1` or later

### Required fields

* `bin`
* `keys`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_GET_BY_KEY_LIST",
    "opValues": {
        "bin": "mapbin",
        "keys": ["a", "c"],
        "mapReturnType": "VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the items associated with the specified keys
{
    "bins": {
        "mapbin": [0, 1] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
*/
```

## MAP_GET_BY_KEY_RANGE

Return map values with keys in the specified range. If `keyBegin` is omitted, all map values with key values less
than `keyEnd` will be returned. If `keyEnd` is omitted, all map values with a key greater than or equal to `keyBegin`
will be returned.

### Required fields

* `bin`
* `mapReturnType`

### Optional fields:

* `keyBegin`
* `keyEnd`

### Example:

```javascript
{
    "operation": "MAP_GET_BY_KEY_RANGE",
    "opValues": {
        "bin": "mapbin",
        "keyBegin": "b",
        "keyEnd": "d",
        "mapReturnType": "KEY_VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the items associated with the specified keys
{
    "bins": {
        "mapbin": [{"b": 2}, {"c": 0}] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
*/
```

## MAP_GET_BY_RANK

Return the map value with the specified rank.

### Required fields

* `bin`
* `rank`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_GET_BY_RANK",
    "opValues": {
        "bin": "mapbin",
        "rank": -2,
        "mapReturnType": "KEY_VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the item with the specified rank
{
    "bins": {
        "mapbin": {"b": 2}
    },
    "generation": 2,
    "ttl": 2592000
}
*/
```

## MAP_GET_BY_RANK_RANGE

Return `count` values from the map beginning with the value with the specified `rank`. If `count` is omitted, all items
with a `rank` greater than or equal to the specified `rank` will be returned.

### Required fields

* `bin`
* `mapReturnType`
* `rank`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "MAP_GET_BY_RANK_RANGE",
    "opValues": {
        "bin": "mapbin",
        "rank": -3,
        "count": 2,
        "mapReturnType": "KEY_VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the item with the specified rank
{
    "bins": {
        "mapbin": [ {"a": 1}, {"b": 2} ] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
*/
```

## MAP_GET_BY_VALUE

Return all map values with the specified value.

### Required fields

* `bin`
* `value`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_GET_BY_VALUE",
    "opValues": {
        "bin": "mapbin",
        "value": 3,
        "mapReturnType": "KEY"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b": 2, "c": 0, "d": 3, "e": 3}
}
The operation will return a value determined by mapReturnType for items with the specified value
{
    "bins": {
        "mapbin": ["d", "e"] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
*/
```

## MAP_GET_BY_VALUE_RANGE

Return all map items with value in the range [`valueBegin`, `valueEnd`). If `valueBegin` is omitted, all map items with
a value less than `valueEnd` will be returned. If `valueEnd` is omitted, all map items with a value greater than or
equal to `valueBegin` will be returned.

### Required fields

* `bin`
* `mapReturnType`

### Optional fields:

* `valueBegin`
* `valueEnd`

### Example:

```javascript
{
    "operation": "MAP_GET_BY_VALUE_RANGE",
    "opValues": {
        "bin": "mapbin",
        "valueBegin": 1,
        "valueEnd": 3,
        "mapReturnType": "KEY_VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the items with values in the specified range
{
    "bins": {
        "mapbin": [ {"a": 1}, {"b": 2} ] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
*/
```

## MAP_GET_BY_VALUE_LIST

Return all map items with a value contained in the provided list of values.

### Required fields

* `bin`
* `values`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_GET_BY_VALUE_LIST",
    "opValues": {
        "bin": "mapbin",
        "values": [2, 3],
        "mapReturnType": "KEY"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3, "e": 3}
}
The operation will return a value determined by mapReturnType for the items with values in the specified
list of values.
{
    "bins": {
        "mapbin": [ "d", "e", "b" ] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
*/
```

## MAP_INCREMENT

Increment the map value with the specified key by the specified amount.

### Required fields

* `bin`
* `incr`
* `key`

### Optional fields:

* `mapPolicy` See [Map Policy](#Map-Policy)

### Example:

```javascript
{
    "operation": "MAP_INCREMENT",
    "opValues": {
        "bin": "mapbin",
        "key": "a",
        "incr": 10
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return the value of the incremented item
{
    "bins": {
        "mapbin": 11
    },
    "generation": 2,
    "ttl": 2592000
}
and the value of the record will be
{
   "mapbin": {"a": 11, "b":2, "c":0, "d":3}
}
*/
```

## MAP_PUT

Store the specified value into the map in the specified bin with the specified key. Equivalent to `Map[key] = value`.

### Required fields

* `bin`
* `key`
* `value`

### Optional fields:

* `mapPolicy` See [Map Policy](#Map-Policy)

### Example:

```javascript
{
    "operation": "MAP_PUT",
    "opValues": {
        "bin": "mapbin",
        "key": "a",
        "value": "new_val"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return the size of the map after setting the item:
{
    "bins": {
        "mapbin": 4
    },
    "generation": 2,
    "ttl": 2592000
}
and the operation the value of the record will be
{
   "mapbin": {"a": "new_val", "b":2, "c":0, "d":3}
}
*/
```

## MAP_PUT_ITEMS

Store multiple values into the map with the specified keys.

### Required fields

* `bin`
* `map`

### Optional fields:

* `mapPolicy` See [Map Policy](#Map-Policy)

### Example:

```javascript
{
    "operation": "MAP_PUT_ITEMS",
    "opValues": {
        "bin": "mapbin",
        "map": {
            "e": 4,
            "f": 5,
            "g": 6
        }
     }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return the size of the map after updating items:
{
    "bins": {
        "mapbin": 7
    },
    "generation": 2,
    "ttl": 2592000
}
and the operation the value of the record will be
{
   "mapbin": {
        "a": "new_val",
        "b":2,
        "c":0,
        "d":3,
        "e": 4,
        "f": 5,
        "g": 6
    }
}
*/
```

## MAP_REMOVE_BY_INDEX

Remove and return the map item at the specified index.

### Required fields

* `bin`
* `index`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_REMOVE_BY_INDEX",
    "opValues": {
        "bin": "map",
        "index": 2,
        "mapReturnType": "KEY_VALUE"
    }
}
```

## MAP_REMOVE_BY_INDEX_RANGE

Remove all map items with indexes in the range `[index, index + count)` . If `count` is omitted, all items beginning
with the item at the specified index will be removed.

### Required fields

* `bin`
* `index`
* `mapReturnType`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "MAP_REMOVE_BY_INDEX_RANGE",
    "opValues": {
        "bin": "map",
        "index": 2,
        "count": 3,
        "mapReturnType": "KEY_VALUE"
    }
}
```

## MAP_REMOVE_BY_KEY

Remove and return the map item with the specified key from the map.

### Required fields

* `bin`
* `key`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_REMOVE_BY_KEY",
    "opValues": {
        "bin": "mapbin",
        "key": "a",
        "mapReturnType": "KEY_VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the item associated with the specified key
{
    "bins": {
        "mapbin":[ {"a": 1} ]
    },
    "generation": 2,
    "ttl": 2592000
}
and the record will be:
{
    "mapbin": {"b":2, "c":0, "d":3}
}
*/
```

## MAP_REMOVE_BY_KEY_RANGE

Remove and return map values with keys in the specified range. If `keyBegin` is omitted, all map values with key values
less than `keyEnd` will be removed and returned. If `keyEnd` is omitted, all map values with a key greater than or equal
to `keyBegin` will be removed and returned.

### Required fields

* `bin`
* `mapReturnType`

### Optional fields:

* `keyBegin`
* `keyEnd`

### Example:

```javascript
{
    "operation": "MAP_REMOVE_BY_KEY_RANGE",
    "opValues": {
        "bin": "mapbin",
        "keyBegin": "a",
        "keyEnd": "c",
        "mapReturnType": "KEY_VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the items with keys in the specified range
{
    "bins": {
        "mapbin":[ {"a": 1}, {"b": 2} ] // this order is not guranteed
    },
    "generation": 2,
    "ttl": 2592000
}
and the record will be:
{
    "mapbin": {"c":0, "d": 3}
}
*/
```

## MAP_REMOVE_BY_RANK

Remove and return the map value with the specified rank.

### Required fields

* `bin`
* `rank`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_REMOVE_BY_RANK",
    "opValues": {
        "bin": "mapbin",
        "rank": -2,
        "mapReturnType": "KEY_VALUE"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the item with the specified rank
{
    "bins": {
        "mapbin":[{"b": 2}]
    },
    "generation": 2,
    "ttl": 2592000
}
and the record will be:
{
    "mapbin": {"a": 1, "c":0, "d":3}
}
*/
```

## MAP_REMOVE_BY_RANK_RANGE

Remove and return `count` values from the map beginning with the value with the specified rank. If `count` is omitted,
all items beginning with the specified `rank` will be removed and returned.

### Required fields

* `bin`
* `rank`
* `mapReturnType`

### Optional fields:

* `count`

### Example:

```javascript
{
    "operation": "MAP_REMOVE_BY_RANK_RANGE",
    "opValues": {
        "bin": "mapbin",
        "rank": -2,
        "count": 2,
        "mapReturnType": "KEY_VALUE"
    }
}

/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3}
}
The operation will return a value determined by mapReturnType for the item with ranks in the specified range
{
    "bins": {
        "mapbin":[{"b": 2}, {"d": 3}] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
and the record will be:
{
    "mapbin": {"a": 1, "c":0}
}
*/
```

## MAP_REMOVE_BY_VALUE

Remove and return all map items with a value equal to the specified value.

### Required fields

* `bin`
* `value`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_REMOVE_BY_VALUE",
    "opValues": {
        "bin": "mapbin",
        "value": 3,
        "mapReturnType": "KEY"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3, "e": 3}
}
The operation will return a value determined by mapReturnType for the items with the specified value.
{
    "bins": {
        "mapbin": ["d", "e"] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
and the record will be:
{
    "mapbin": {"a": 1, "b": 2, "c":0}
}
*/
```

## MAP_REMOVE_BY_VALUE_RANGE

Remove and return all map items with value in the range `[valueBegin, valueEnd)`. If `valueBegin` is omitted, all map
items with a value less than `valueEnd` will be removed and returned. If `valueEnd` is omitted, all map items with a
value greater than or equal to `valueBegin` will be removed and returned.

### Required fields

* `bin`
* `mapReturnType`

### Optional fields:

* `valueBegin`
* `valueEnd`

### Example:

```javascript
{
    "operation": "MAP_REMOVE_BY_VALUE_RANGE",
    "opValues": {
        "bin": "mapbin",
        "valueBegin": 1,
        "valueEnd": 4,
        "mapReturnType": "KEY"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3, "e": 3}
}
The operation will return a value determined by mapReturnType for the items with values in the specified
range of values
{
    "bins": {
        "mapbin": ["d", "e", "b", "a"] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
and the record will be:
{
    "mapbin": {"c":0}
}
*/
```

## MAP_REMOVE_BY_VALUE_LIST

Remove and return all map items with a value contained in the provided list of values.

Requires Aerospike Server `3.16.0.1` or later.

### Required fields

* `bin`
* `values`
* `mapReturnType`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_REMOVE_BY_VALUE_LIST",
    "opValues": {
        "bin": "mapbin",
        "values": [0, 3],
        "mapReturnType": "KEY"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3, "e": 3}
}
The operation will return a value determined by mapReturnType for the items with values in the specified
list of values.
{
    "bins": {
        "mapbin": ["d", "e", "c"] // This ordering is not guaranteed
    },
    "generation": 2,
    "ttl": 2592000
}
and the record will be:
{
    "mapbin": {"a": 1, "b": 2}
}
*/
```

## MAP_SET_POLICY

Set the policy for the map in the specified bin.

### Required fields

* `bin`
* `mapPolicy`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_SET_MAP_POLICY",
    "opValues": {
        "bin": "map",
        "mapPolicy": {
            "order": "KEY_ORDERED",
            "writeMode": "UPDATE"
        }
    }
}
```

## MAP_SIZE

Return the size of the map in the specified bin.

### Required fields

* `bin`

### Optional fields:

* None

### Example:

```javascript
{
    "operation": "MAP_SIZE",
    "opValues": {
        "bin": "mapbin"
    }
}
/*If prior to the operation the stored record was:
{
    "mapbin": {"a": 1, "b":2, "c":0, "d":3, "e": 3}
}
The operation will return the size of the map
{
    "bins": {
        "mapbin": 5
    },
    "generation": 2,
    "ttl": 2592000
}
*/
```

## Policies

### Map Policy

A Map policy alters the manner in which map updates are handled. It has three distinct fields:

* order : This defines the order of the map. Must be one of "UNORDERED", "KEY_ORDERED", or "KEY_VALUE_ORDERED"
* writeMode : Defines how changes to existing in the map will be handled. Options are: "CREATE_ONLY", or "UPDATE_ONLY"
    * "CREATE_ONLY" If the key already exists, the write will fail.
    * "UPDATE": If the key already exists, the item will be overwritten.
    * "UPDATE_ONLY" If the key already exists, the item will be overwritten.

* writeFlags : A list of Strings indicating how to handle map writes. The usage of this option requires Aerospike
  Server >= `4.3.0.0`. Possibles values are:
    * "DEFAULT" Allow writes to existing keys and creation of new keys.
    * "CREATE_ONLY": If the key already exists, the item will be denied.
    * "UPDATE_ONLY": If the key already exists, the item will be overwritten. Creation of new keys is denied.
    * "PARTIAL": Allow other valid map items to be committed if a map item is denied due to write flag constraints.
    * "NO_FAIL": Do not raise error if a map item is denied due to write flag constraints.

Only one of `writeFlags` or `writeMode` should be used. `writeFlags` should be preferred if using an Aerospike Server >
= `4.3.0.0`.

```javascript
"mapPolicy": {
    "order": "KEY_ORDERED",
    "writeMode": "UPDATE", // Either writeMode should be provided or writeFlags
    "writeFlags": ["UPDATE_ONLY", "PARTIAL", "NO_FAIL"] // Either writeMode should be provided or writeFlags
}
```
