import * as I from 'immutable';

export type ValuesOf<T extends any[]> = T[number] extends
    | boolean
    | string
    | number
    | Function
    | null
    | undefined
    ? T[number]
    : TypedMap<T[number]>;

/**
 * This is a bit of conditional typing to convert objects to TypedMaps, arrays to Lists, but leave
 * other types alone.
 */
type Immutate<TMaybeImmutable> = TMaybeImmutable extends any[]
    ? I.List<ValuesOf<TMaybeImmutable>> | undefined
    : TMaybeImmutable extends boolean | string | number | Function | null | undefined
        ? TMaybeImmutable | undefined
        : TypedMap<TMaybeImmutable> | undefined;

/**
 * ideas on typing from https://blog.mgechev.com/2018/01/18/react-typescript-redux-immutable/
 *
 * NOTE: this interface contains a much more strongly typed version of immutable map. More definitions
 * cribbed from the map interfaces (such as merge, mergeIn etc) can be added if needed. Updates can be
 * replaced with sets.
 *
 * This is optimized for use in the API layer and does some tricks to allow for good typing of immutable maps
 * representing objects.
 *
 * The main one is that the TData will always be the base type but if that base type is an object it will be typed as
 * a TypedMap on get. This is not a cast but just a trick for typing. The underlying implementation is the plain map from
 * Immutable JS.
 *
 * Also you will find there are only 4 deep xxxIn keys. If you need to go deeper another level should be added for get,
 * set, has, and remove.
 *
 * These xxxIn methods will not work at all if there are optional fields in the key path.
 */
export interface TypedMap<TData> {
    toJS(): TData;

    has<TKey extends keyof TData>(key: TKey): boolean;
    /**
     * True if the result of following a path of keys or indices through nested
     * Iterables results in a set value.
     */

    hasIn<TKey1 extends keyof TData>(keys: [TKey1]): boolean;
    hasIn<TKey1 extends keyof TData, TKey2 extends keyof TData[TKey1]>(
        keys: [TKey1, TKey2]
    ): boolean;
    hasIn<
        TKey1 extends keyof TData,
        TKey2 extends keyof TData[TKey1],
        TKey3 extends keyof TData[TKey1][TKey2]
    >(
        keys: [TKey1, TKey2, TKey3]
    ): boolean;

    hasIn<
        TKey1 extends keyof TData,
        TKey2 extends keyof TData[TKey1],
        TKey3 extends keyof TData[TKey1][TKey2],
        TKey4 extends keyof TData[TKey1][TKey2][TKey3]
    >(
        keys: [TKey1, TKey2, TKey3, TKey4]
    ): boolean;

    get<TKey extends keyof TData>(key: TKey): Immutate<TData[TKey]>;

    /**
     * Returns the value found by following a path of keys or indices through
     * nested Iterables.
     */

    getIn<TKey1 extends keyof TData>(keys: [TKey1]): Immutate<TData[TKey1]>;
    getIn<TKey1 extends keyof TData, TKey2 extends keyof TData[TKey1]>(
        keys: [TKey1, TKey2]
    ): Immutate<TData[TKey1][TKey2]>;
    getIn<
        TKey1 extends keyof TData,
        TKey2 extends keyof TData[TKey1],
        TKey3 extends keyof TData[TKey1][TKey2]
    >(
        keys: [TKey1, TKey2, TKey3]
    ): Immutate<TData[TKey1][TKey2][TKey3]>;

    getIn<
        TKey1 extends keyof TData,
        TKey2 extends keyof TData[TKey1],
        TKey3 extends keyof TData[TKey1][TKey2],
        TKey4 extends keyof TData[TKey1][TKey2][TKey3]
    >(
        keys: [TKey1, TKey2, TKey3, TKey4]
    ): Immutate<TData[TKey1][TKey2][TKey3][TKey4]>;

    set<TKey extends keyof TData, TVal extends Immutate<TData[TKey]>>(
        key: TKey,
        value: TVal
    ): TypedMap<TData>;
    /**
     * Returns a new Map having set `value` at this `key`. If any keys in
     * `keys` do not exist, a new immutable Map will be created at that key.
     */
    setIn<TKey1 extends keyof TData, V extends Immutate<TData[TKey1]>>(
        keys: [TKey1],
        val: V
    ): TypedMap<TData>;
    setIn<
        TKey1 extends keyof TData,
        TKey2 extends keyof TData[TKey1],
        V extends Immutate<TData[TKey1][TKey2]>
    >(
        keys: [TKey1, TKey2],
        val: V
    ): TypedMap<TData>;
    setIn<
        TKey1 extends keyof TData,
        TKey2 extends keyof TData[TKey1],
        TKey3 extends keyof TData[TKey1][TKey2],
        V extends Immutate<TData[TKey1][TKey2][TKey3]>
    >(
        keys: [TKey1, TKey2, TKey3],
        val: V
    ): TypedMap<TData>;

    setIn<
        TKey1 extends keyof TData,
        TKey2 extends keyof TData[TKey1],
        TKey3 extends keyof TData[TKey1][TKey2],
        TKey4 extends keyof TData[TKey1][TKey2][TKey3],
        V extends Immutate<TData[TKey1][TKey2][TKey3][TKey4]>
    >(
        keys: [TKey1, TKey2, TKey3, TKey4],
        val: V
    ): TypedMap<TData>;

    /**
     * Returns a new Map having removed the value at this `keys`. If any keys
     * in `keys` do not exist, no change will occur.
     *
     */
    removeIn<TKey1 extends keyof TData>(keys: [TKey1]): TypedMap<TData>;
    removeIn<TKey1 extends keyof TData, TKey2 extends keyof TData[TKey1]>(
        keys: [TKey1, TKey2]
    ): TypedMap<TData>;
    removeIn<
        TKey1 extends keyof TData,
        TKey2 extends keyof TData[TKey1],
        TKey3 extends keyof TData[TKey1][TKey2]
    >(
        keys: [TKey1, TKey2, TKey3]
    ): TypedMap<TData>;

    removeIn<
        TKey1 extends keyof TData,
        TKey2 extends keyof TData[TKey1],
        TKey3 extends keyof TData[TKey1][TKey2],
        TKey4 extends keyof TData[TKey1][TKey2][TKey3]
    >(
        keys: [TKey1, TKey2, TKey3, TKey4]
    ): TypedMap<TData>;
    toJS(): TData;

    /**
     * Every time you call one of the above functions, a new immutable Map is
     * created. If a pure function calls a number of these to produce a final
     * return value, then a penalty on performance and memory has been paid by
     * creating all of the intermediate immutable Maps.
     *
     * If you need to apply a series of mutations to produce a new immutable
     * Map, `withMutations()` creates a temporary mutable copy of the Map which
     * can apply mutations in a highly performant manner. In fact, this is
     * exactly how complex mutations like `merge` are done.
     *
     * As an example, this results in the creation of 2, not 4, new Maps:
     *
     *     var map1 = Immutable.Map();
     *     var map2 = map1.withMutations(map => {
     *       map.set('a', 1).set('b', 2).set('c', 3);
     *     });
     *     assert(map1.size === 0);
     *     assert(map2.size === 3);
     *
     * Note: Not all methods can be used on a mutable collection or within
     * `withMutations`! Only `set` and `merge` may be used mutatively.
     *
     */

    withMutations(cb: (r: TypedMap<TData>) => TypedMap<TData>): TypedMap<TData>;

    reduce<R, K, V>(
        reducer: (reduction?: R, value?: V, key?: K, iter?: /*this*/ I.Iterable<K, V>) => R,
        initialReduction?: R,
        context?: any
    ): R;

    size: number;
}

/**
 * Creates a TypedMap with the same fields as the initial type.
 * Underlying implementation is an ImmutibleJS map but it goes through the TypedMap interface.
 *
 * @param initial interface/type model for map
 */
export const createTypedMap = <TData>(initial?: TData) => {
    if (initial === undefined) {
        return I.Map() as TypedMap<TData>;
    }
    return I.Map<keyof TData, any>(initial) as TypedMap<TData>;
};
