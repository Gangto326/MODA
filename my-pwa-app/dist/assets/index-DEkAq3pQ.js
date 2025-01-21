(function () {
  const t = document.createElement("link").relList;
  if (t && t.supports && t.supports("modulepreload")) return;
  for (const l of document.querySelectorAll('link[rel="modulepreload"]')) r(l);
  new MutationObserver((l) => {
    for (const u of l)
      if (u.type === "childList")
        for (const i of u.addedNodes)
          i.tagName === "LINK" && i.rel === "modulepreload" && r(i);
  }).observe(document, { childList: !0, subtree: !0 });
  function n(l) {
    const u = {};
    return (
      l.integrity && (u.integrity = l.integrity),
      l.referrerPolicy && (u.referrerPolicy = l.referrerPolicy),
      l.crossOrigin === "use-credentials"
        ? (u.credentials = "include")
        : l.crossOrigin === "anonymous"
        ? (u.credentials = "omit")
        : (u.credentials = "same-origin"),
      u
    );
  }
  function r(l) {
    if (l.ep) return;
    l.ep = !0;
    const u = n(l);
    fetch(l.href, u);
  }
})();
function af(e) {
  return e && e.__esModule && Object.prototype.hasOwnProperty.call(e, "default")
    ? e.default
    : e;
}
var Na = { exports: {} },
  gl = {},
  Ta = { exports: {} },
  O = {};
/**
 * @license React
 * react.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */ var ur = Symbol.for("react.element"),
  sf = Symbol.for("react.portal"),
  cf = Symbol.for("react.fragment"),
  ff = Symbol.for("react.strict_mode"),
  df = Symbol.for("react.profiler"),
  pf = Symbol.for("react.provider"),
  hf = Symbol.for("react.context"),
  mf = Symbol.for("react.forward_ref"),
  yf = Symbol.for("react.suspense"),
  vf = Symbol.for("react.memo"),
  gf = Symbol.for("react.lazy"),
  co = Symbol.iterator;
function wf(e) {
  return e === null || typeof e != "object"
    ? null
    : ((e = (co && e[co]) || e["@@iterator"]),
      typeof e == "function" ? e : null);
}
var za = {
    isMounted: function () {
      return !1;
    },
    enqueueForceUpdate: function () {},
    enqueueReplaceState: function () {},
    enqueueSetState: function () {},
  },
  Oa = Object.assign,
  Ia = {};
function dn(e, t, n) {
  (this.props = e),
    (this.context = t),
    (this.refs = Ia),
    (this.updater = n || za);
}
dn.prototype.isReactComponent = {};
dn.prototype.setState = function (e, t) {
  if (typeof e != "object" && typeof e != "function" && e != null)
    throw Error(
      "setState(...): takes an object of state variables to update or a function which returns an object of state variables."
    );
  this.updater.enqueueSetState(this, e, t, "setState");
};
dn.prototype.forceUpdate = function (e) {
  this.updater.enqueueForceUpdate(this, e, "forceUpdate");
};
function Da() {}
Da.prototype = dn.prototype;
function si(e, t, n) {
  (this.props = e),
    (this.context = t),
    (this.refs = Ia),
    (this.updater = n || za);
}
var ci = (si.prototype = new Da());
ci.constructor = si;
Oa(ci, dn.prototype);
ci.isPureReactComponent = !0;
var fo = Array.isArray,
  Fa = Object.prototype.hasOwnProperty,
  fi = { current: null },
  Ma = { key: !0, ref: !0, __self: !0, __source: !0 };
function $a(e, t, n) {
  var r,
    l = {},
    u = null,
    i = null;
  if (t != null)
    for (r in (t.ref !== void 0 && (i = t.ref),
    t.key !== void 0 && (u = "" + t.key),
    t))
      Fa.call(t, r) && !Ma.hasOwnProperty(r) && (l[r] = t[r]);
  var o = arguments.length - 2;
  if (o === 1) l.children = n;
  else if (1 < o) {
    for (var a = Array(o), s = 0; s < o; s++) a[s] = arguments[s + 2];
    l.children = a;
  }
  if (e && e.defaultProps)
    for (r in ((o = e.defaultProps), o)) l[r] === void 0 && (l[r] = o[r]);
  return {
    $$typeof: ur,
    type: e,
    key: u,
    ref: i,
    props: l,
    _owner: fi.current,
  };
}
function Sf(e, t) {
  return {
    $$typeof: ur,
    type: e.type,
    key: t,
    ref: e.ref,
    props: e.props,
    _owner: e._owner,
  };
}
function di(e) {
  return typeof e == "object" && e !== null && e.$$typeof === ur;
}
function kf(e) {
  var t = { "=": "=0", ":": "=2" };
  return (
    "$" +
    e.replace(/[=:]/g, function (n) {
      return t[n];
    })
  );
}
var po = /\/+/g;
function Ul(e, t) {
  return typeof e == "object" && e !== null && e.key != null
    ? kf("" + e.key)
    : t.toString(36);
}
function Ir(e, t, n, r, l) {
  var u = typeof e;
  (u === "undefined" || u === "boolean") && (e = null);
  var i = !1;
  if (e === null) i = !0;
  else
    switch (u) {
      case "string":
      case "number":
        i = !0;
        break;
      case "object":
        switch (e.$$typeof) {
          case ur:
          case sf:
            i = !0;
        }
    }
  if (i)
    return (
      (i = e),
      (l = l(i)),
      (e = r === "" ? "." + Ul(i, 0) : r),
      fo(l)
        ? ((n = ""),
          e != null && (n = e.replace(po, "$&/") + "/"),
          Ir(l, t, n, "", function (s) {
            return s;
          }))
        : l != null &&
          (di(l) &&
            (l = Sf(
              l,
              n +
                (!l.key || (i && i.key === l.key)
                  ? ""
                  : ("" + l.key).replace(po, "$&/") + "/") +
                e
            )),
          t.push(l)),
      1
    );
  if (((i = 0), (r = r === "" ? "." : r + ":"), fo(e)))
    for (var o = 0; o < e.length; o++) {
      u = e[o];
      var a = r + Ul(u, o);
      i += Ir(u, t, n, a, l);
    }
  else if (((a = wf(e)), typeof a == "function"))
    for (e = a.call(e), o = 0; !(u = e.next()).done; )
      (u = u.value), (a = r + Ul(u, o++)), (i += Ir(u, t, n, a, l));
  else if (u === "object")
    throw (
      ((t = String(e)),
      Error(
        "Objects are not valid as a React child (found: " +
          (t === "[object Object]"
            ? "object with keys {" + Object.keys(e).join(", ") + "}"
            : t) +
          "). If you meant to render a collection of children, use an array instead."
      ))
    );
  return i;
}
function mr(e, t, n) {
  if (e == null) return e;
  var r = [],
    l = 0;
  return (
    Ir(e, r, "", "", function (u) {
      return t.call(n, u, l++);
    }),
    r
  );
}
function Ef(e) {
  if (e._status === -1) {
    var t = e._result;
    (t = t()),
      t.then(
        function (n) {
          (e._status === 0 || e._status === -1) &&
            ((e._status = 1), (e._result = n));
        },
        function (n) {
          (e._status === 0 || e._status === -1) &&
            ((e._status = 2), (e._result = n));
        }
      ),
      e._status === -1 && ((e._status = 0), (e._result = t));
  }
  if (e._status === 1) return e._result.default;
  throw e._result;
}
var ae = { current: null },
  Dr = { transition: null },
  xf = {
    ReactCurrentDispatcher: ae,
    ReactCurrentBatchConfig: Dr,
    ReactCurrentOwner: fi,
  };
function ja() {
  throw Error("act(...) is not supported in production builds of React.");
}
O.Children = {
  map: mr,
  forEach: function (e, t, n) {
    mr(
      e,
      function () {
        t.apply(this, arguments);
      },
      n
    );
  },
  count: function (e) {
    var t = 0;
    return (
      mr(e, function () {
        t++;
      }),
      t
    );
  },
  toArray: function (e) {
    return (
      mr(e, function (t) {
        return t;
      }) || []
    );
  },
  only: function (e) {
    if (!di(e))
      throw Error(
        "React.Children.only expected to receive a single React element child."
      );
    return e;
  },
};
O.Component = dn;
O.Fragment = cf;
O.Profiler = df;
O.PureComponent = si;
O.StrictMode = ff;
O.Suspense = yf;
O.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED = xf;
O.act = ja;
O.cloneElement = function (e, t, n) {
  if (e == null)
    throw Error(
      "React.cloneElement(...): The argument must be a React element, but you passed " +
        e +
        "."
    );
  var r = Oa({}, e.props),
    l = e.key,
    u = e.ref,
    i = e._owner;
  if (t != null) {
    if (
      (t.ref !== void 0 && ((u = t.ref), (i = fi.current)),
      t.key !== void 0 && (l = "" + t.key),
      e.type && e.type.defaultProps)
    )
      var o = e.type.defaultProps;
    for (a in t)
      Fa.call(t, a) &&
        !Ma.hasOwnProperty(a) &&
        (r[a] = t[a] === void 0 && o !== void 0 ? o[a] : t[a]);
  }
  var a = arguments.length - 2;
  if (a === 1) r.children = n;
  else if (1 < a) {
    o = Array(a);
    for (var s = 0; s < a; s++) o[s] = arguments[s + 2];
    r.children = o;
  }
  return { $$typeof: ur, type: e.type, key: l, ref: u, props: r, _owner: i };
};
O.createContext = function (e) {
  return (
    (e = {
      $$typeof: hf,
      _currentValue: e,
      _currentValue2: e,
      _threadCount: 0,
      Provider: null,
      Consumer: null,
      _defaultValue: null,
      _globalName: null,
    }),
    (e.Provider = { $$typeof: pf, _context: e }),
    (e.Consumer = e)
  );
};
O.createElement = $a;
O.createFactory = function (e) {
  var t = $a.bind(null, e);
  return (t.type = e), t;
};
O.createRef = function () {
  return { current: null };
};
O.forwardRef = function (e) {
  return { $$typeof: mf, render: e };
};
O.isValidElement = di;
O.lazy = function (e) {
  return { $$typeof: gf, _payload: { _status: -1, _result: e }, _init: Ef };
};
O.memo = function (e, t) {
  return { $$typeof: vf, type: e, compare: t === void 0 ? null : t };
};
O.startTransition = function (e) {
  var t = Dr.transition;
  Dr.transition = {};
  try {
    e();
  } finally {
    Dr.transition = t;
  }
};
O.unstable_act = ja;
O.useCallback = function (e, t) {
  return ae.current.useCallback(e, t);
};
O.useContext = function (e) {
  return ae.current.useContext(e);
};
O.useDebugValue = function () {};
O.useDeferredValue = function (e) {
  return ae.current.useDeferredValue(e);
};
O.useEffect = function (e, t) {
  return ae.current.useEffect(e, t);
};
O.useId = function () {
  return ae.current.useId();
};
O.useImperativeHandle = function (e, t, n) {
  return ae.current.useImperativeHandle(e, t, n);
};
O.useInsertionEffect = function (e, t) {
  return ae.current.useInsertionEffect(e, t);
};
O.useLayoutEffect = function (e, t) {
  return ae.current.useLayoutEffect(e, t);
};
O.useMemo = function (e, t) {
  return ae.current.useMemo(e, t);
};
O.useReducer = function (e, t, n) {
  return ae.current.useReducer(e, t, n);
};
O.useRef = function (e) {
  return ae.current.useRef(e);
};
O.useState = function (e) {
  return ae.current.useState(e);
};
O.useSyncExternalStore = function (e, t, n) {
  return ae.current.useSyncExternalStore(e, t, n);
};
O.useTransition = function () {
  return ae.current.useTransition();
};
O.version = "18.3.1";
Ta.exports = O;
var v = Ta.exports;
const ho = af(v);
/**
 * @license React
 * react-jsx-runtime.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */ var Cf = v,
  Pf = Symbol.for("react.element"),
  _f = Symbol.for("react.fragment"),
  Rf = Object.prototype.hasOwnProperty,
  Lf = Cf.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED.ReactCurrentOwner,
  Nf = { key: !0, ref: !0, __self: !0, __source: !0 };
function Ua(e, t, n) {
  var r,
    l = {},
    u = null,
    i = null;
  n !== void 0 && (u = "" + n),
    t.key !== void 0 && (u = "" + t.key),
    t.ref !== void 0 && (i = t.ref);
  for (r in t) Rf.call(t, r) && !Nf.hasOwnProperty(r) && (l[r] = t[r]);
  if (e && e.defaultProps)
    for (r in ((t = e.defaultProps), t)) l[r] === void 0 && (l[r] = t[r]);
  return {
    $$typeof: Pf,
    type: e,
    key: u,
    ref: i,
    props: l,
    _owner: Lf.current,
  };
}
gl.Fragment = _f;
gl.jsx = Ua;
gl.jsxs = Ua;
Na.exports = gl;
var We = Na.exports,
  pu = {},
  Aa = { exports: {} },
  Se = {},
  Ba = { exports: {} },
  Ha = {};
/**
 * @license React
 * scheduler.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */ (function (e) {
  function t(P, T) {
    var z = P.length;
    P.push(T);
    e: for (; 0 < z; ) {
      var Q = (z - 1) >>> 1,
        Z = P[Q];
      if (0 < l(Z, T)) (P[Q] = T), (P[z] = Z), (z = Q);
      else break e;
    }
  }
  function n(P) {
    return P.length === 0 ? null : P[0];
  }
  function r(P) {
    if (P.length === 0) return null;
    var T = P[0],
      z = P.pop();
    if (z !== T) {
      P[0] = z;
      e: for (var Q = 0, Z = P.length, pr = Z >>> 1; Q < pr; ) {
        var xt = 2 * (Q + 1) - 1,
          jl = P[xt],
          Ct = xt + 1,
          hr = P[Ct];
        if (0 > l(jl, z))
          Ct < Z && 0 > l(hr, jl)
            ? ((P[Q] = hr), (P[Ct] = z), (Q = Ct))
            : ((P[Q] = jl), (P[xt] = z), (Q = xt));
        else if (Ct < Z && 0 > l(hr, z)) (P[Q] = hr), (P[Ct] = z), (Q = Ct);
        else break e;
      }
    }
    return T;
  }
  function l(P, T) {
    var z = P.sortIndex - T.sortIndex;
    return z !== 0 ? z : P.id - T.id;
  }
  if (typeof performance == "object" && typeof performance.now == "function") {
    var u = performance;
    e.unstable_now = function () {
      return u.now();
    };
  } else {
    var i = Date,
      o = i.now();
    e.unstable_now = function () {
      return i.now() - o;
    };
  }
  var a = [],
    s = [],
    h = 1,
    m = null,
    p = 3,
    y = !1,
    S = !1,
    g = !1,
    x = typeof setTimeout == "function" ? setTimeout : null,
    f = typeof clearTimeout == "function" ? clearTimeout : null,
    c = typeof setImmediate < "u" ? setImmediate : null;
  typeof navigator < "u" &&
    navigator.scheduling !== void 0 &&
    navigator.scheduling.isInputPending !== void 0 &&
    navigator.scheduling.isInputPending.bind(navigator.scheduling);
  function d(P) {
    for (var T = n(s); T !== null; ) {
      if (T.callback === null) r(s);
      else if (T.startTime <= P)
        r(s), (T.sortIndex = T.expirationTime), t(a, T);
      else break;
      T = n(s);
    }
  }
  function w(P) {
    if (((g = !1), d(P), !S))
      if (n(a) !== null) (S = !0), Ml(E);
      else {
        var T = n(s);
        T !== null && $l(w, T.startTime - P);
      }
  }
  function E(P, T) {
    (S = !1), g && ((g = !1), f(L), (L = -1)), (y = !0);
    var z = p;
    try {
      for (
        d(T), m = n(a);
        m !== null && (!(m.expirationTime > T) || (P && !ce()));

      ) {
        var Q = m.callback;
        if (typeof Q == "function") {
          (m.callback = null), (p = m.priorityLevel);
          var Z = Q(m.expirationTime <= T);
          (T = e.unstable_now()),
            typeof Z == "function" ? (m.callback = Z) : m === n(a) && r(a),
            d(T);
        } else r(a);
        m = n(a);
      }
      if (m !== null) var pr = !0;
      else {
        var xt = n(s);
        xt !== null && $l(w, xt.startTime - T), (pr = !1);
      }
      return pr;
    } finally {
      (m = null), (p = z), (y = !1);
    }
  }
  var R = !1,
    _ = null,
    L = -1,
    I = 5,
    N = -1;
  function ce() {
    return !(e.unstable_now() - N < I);
  }
  function vn() {
    if (_ !== null) {
      var P = e.unstable_now();
      N = P;
      var T = !0;
      try {
        T = _(!0, P);
      } finally {
        T ? gn() : ((R = !1), (_ = null));
      }
    } else R = !1;
  }
  var gn;
  if (typeof c == "function")
    gn = function () {
      c(vn);
    };
  else if (typeof MessageChannel < "u") {
    var so = new MessageChannel(),
      of = so.port2;
    (so.port1.onmessage = vn),
      (gn = function () {
        of.postMessage(null);
      });
  } else
    gn = function () {
      x(vn, 0);
    };
  function Ml(P) {
    (_ = P), R || ((R = !0), gn());
  }
  function $l(P, T) {
    L = x(function () {
      P(e.unstable_now());
    }, T);
  }
  (e.unstable_IdlePriority = 5),
    (e.unstable_ImmediatePriority = 1),
    (e.unstable_LowPriority = 4),
    (e.unstable_NormalPriority = 3),
    (e.unstable_Profiling = null),
    (e.unstable_UserBlockingPriority = 2),
    (e.unstable_cancelCallback = function (P) {
      P.callback = null;
    }),
    (e.unstable_continueExecution = function () {
      S || y || ((S = !0), Ml(E));
    }),
    (e.unstable_forceFrameRate = function (P) {
      0 > P || 125 < P
        ? console.error(
            "forceFrameRate takes a positive int between 0 and 125, forcing frame rates higher than 125 fps is not supported"
          )
        : (I = 0 < P ? Math.floor(1e3 / P) : 5);
    }),
    (e.unstable_getCurrentPriorityLevel = function () {
      return p;
    }),
    (e.unstable_getFirstCallbackNode = function () {
      return n(a);
    }),
    (e.unstable_next = function (P) {
      switch (p) {
        case 1:
        case 2:
        case 3:
          var T = 3;
          break;
        default:
          T = p;
      }
      var z = p;
      p = T;
      try {
        return P();
      } finally {
        p = z;
      }
    }),
    (e.unstable_pauseExecution = function () {}),
    (e.unstable_requestPaint = function () {}),
    (e.unstable_runWithPriority = function (P, T) {
      switch (P) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
          break;
        default:
          P = 3;
      }
      var z = p;
      p = P;
      try {
        return T();
      } finally {
        p = z;
      }
    }),
    (e.unstable_scheduleCallback = function (P, T, z) {
      var Q = e.unstable_now();
      switch (
        (typeof z == "object" && z !== null
          ? ((z = z.delay), (z = typeof z == "number" && 0 < z ? Q + z : Q))
          : (z = Q),
        P)
      ) {
        case 1:
          var Z = -1;
          break;
        case 2:
          Z = 250;
          break;
        case 5:
          Z = 1073741823;
          break;
        case 4:
          Z = 1e4;
          break;
        default:
          Z = 5e3;
      }
      return (
        (Z = z + Z),
        (P = {
          id: h++,
          callback: T,
          priorityLevel: P,
          startTime: z,
          expirationTime: Z,
          sortIndex: -1,
        }),
        z > Q
          ? ((P.sortIndex = z),
            t(s, P),
            n(a) === null &&
              P === n(s) &&
              (g ? (f(L), (L = -1)) : (g = !0), $l(w, z - Q)))
          : ((P.sortIndex = Z), t(a, P), S || y || ((S = !0), Ml(E))),
        P
      );
    }),
    (e.unstable_shouldYield = ce),
    (e.unstable_wrapCallback = function (P) {
      var T = p;
      return function () {
        var z = p;
        p = T;
        try {
          return P.apply(this, arguments);
        } finally {
          p = z;
        }
      };
    });
})(Ha);
Ba.exports = Ha;
var Tf = Ba.exports;
/**
 * @license React
 * react-dom.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */ var zf = v,
  we = Tf;
function k(e) {
  for (
    var t = "https://reactjs.org/docs/error-decoder.html?invariant=" + e, n = 1;
    n < arguments.length;
    n++
  )
    t += "&args[]=" + encodeURIComponent(arguments[n]);
  return (
    "Minified React error #" +
    e +
    "; visit " +
    t +
    " for the full message or use the non-minified dev environment for full errors and additional helpful warnings."
  );
}
var Va = new Set(),
  Bn = {};
function $t(e, t) {
  ln(e, t), ln(e + "Capture", t);
}
function ln(e, t) {
  for (Bn[e] = t, e = 0; e < t.length; e++) Va.add(t[e]);
}
var Ge = !(
    typeof window > "u" ||
    typeof window.document > "u" ||
    typeof window.document.createElement > "u"
  ),
  hu = Object.prototype.hasOwnProperty,
  Of =
    /^[:A-Z_a-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD][:A-Z_a-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD\-.0-9\u00B7\u0300-\u036F\u203F-\u2040]*$/,
  mo = {},
  yo = {};
function If(e) {
  return hu.call(yo, e)
    ? !0
    : hu.call(mo, e)
    ? !1
    : Of.test(e)
    ? (yo[e] = !0)
    : ((mo[e] = !0), !1);
}
function Df(e, t, n, r) {
  if (n !== null && n.type === 0) return !1;
  switch (typeof t) {
    case "function":
    case "symbol":
      return !0;
    case "boolean":
      return r
        ? !1
        : n !== null
        ? !n.acceptsBooleans
        : ((e = e.toLowerCase().slice(0, 5)), e !== "data-" && e !== "aria-");
    default:
      return !1;
  }
}
function Ff(e, t, n, r) {
  if (t === null || typeof t > "u" || Df(e, t, n, r)) return !0;
  if (r) return !1;
  if (n !== null)
    switch (n.type) {
      case 3:
        return !t;
      case 4:
        return t === !1;
      case 5:
        return isNaN(t);
      case 6:
        return isNaN(t) || 1 > t;
    }
  return !1;
}
function se(e, t, n, r, l, u, i) {
  (this.acceptsBooleans = t === 2 || t === 3 || t === 4),
    (this.attributeName = r),
    (this.attributeNamespace = l),
    (this.mustUseProperty = n),
    (this.propertyName = e),
    (this.type = t),
    (this.sanitizeURL = u),
    (this.removeEmptyString = i);
}
var te = {};
"children dangerouslySetInnerHTML defaultValue defaultChecked innerHTML suppressContentEditableWarning suppressHydrationWarning style"
  .split(" ")
  .forEach(function (e) {
    te[e] = new se(e, 0, !1, e, null, !1, !1);
  });
[
  ["acceptCharset", "accept-charset"],
  ["className", "class"],
  ["htmlFor", "for"],
  ["httpEquiv", "http-equiv"],
].forEach(function (e) {
  var t = e[0];
  te[t] = new se(t, 1, !1, e[1], null, !1, !1);
});
["contentEditable", "draggable", "spellCheck", "value"].forEach(function (e) {
  te[e] = new se(e, 2, !1, e.toLowerCase(), null, !1, !1);
});
[
  "autoReverse",
  "externalResourcesRequired",
  "focusable",
  "preserveAlpha",
].forEach(function (e) {
  te[e] = new se(e, 2, !1, e, null, !1, !1);
});
"allowFullScreen async autoFocus autoPlay controls default defer disabled disablePictureInPicture disableRemotePlayback formNoValidate hidden loop noModule noValidate open playsInline readOnly required reversed scoped seamless itemScope"
  .split(" ")
  .forEach(function (e) {
    te[e] = new se(e, 3, !1, e.toLowerCase(), null, !1, !1);
  });
["checked", "multiple", "muted", "selected"].forEach(function (e) {
  te[e] = new se(e, 3, !0, e, null, !1, !1);
});
["capture", "download"].forEach(function (e) {
  te[e] = new se(e, 4, !1, e, null, !1, !1);
});
["cols", "rows", "size", "span"].forEach(function (e) {
  te[e] = new se(e, 6, !1, e, null, !1, !1);
});
["rowSpan", "start"].forEach(function (e) {
  te[e] = new se(e, 5, !1, e.toLowerCase(), null, !1, !1);
});
var pi = /[\-:]([a-z])/g;
function hi(e) {
  return e[1].toUpperCase();
}
"accent-height alignment-baseline arabic-form baseline-shift cap-height clip-path clip-rule color-interpolation color-interpolation-filters color-profile color-rendering dominant-baseline enable-background fill-opacity fill-rule flood-color flood-opacity font-family font-size font-size-adjust font-stretch font-style font-variant font-weight glyph-name glyph-orientation-horizontal glyph-orientation-vertical horiz-adv-x horiz-origin-x image-rendering letter-spacing lighting-color marker-end marker-mid marker-start overline-position overline-thickness paint-order panose-1 pointer-events rendering-intent shape-rendering stop-color stop-opacity strikethrough-position strikethrough-thickness stroke-dasharray stroke-dashoffset stroke-linecap stroke-linejoin stroke-miterlimit stroke-opacity stroke-width text-anchor text-decoration text-rendering underline-position underline-thickness unicode-bidi unicode-range units-per-em v-alphabetic v-hanging v-ideographic v-mathematical vector-effect vert-adv-y vert-origin-x vert-origin-y word-spacing writing-mode xmlns:xlink x-height"
  .split(" ")
  .forEach(function (e) {
    var t = e.replace(pi, hi);
    te[t] = new se(t, 1, !1, e, null, !1, !1);
  });
"xlink:actuate xlink:arcrole xlink:role xlink:show xlink:title xlink:type"
  .split(" ")
  .forEach(function (e) {
    var t = e.replace(pi, hi);
    te[t] = new se(t, 1, !1, e, "http://www.w3.org/1999/xlink", !1, !1);
  });
["xml:base", "xml:lang", "xml:space"].forEach(function (e) {
  var t = e.replace(pi, hi);
  te[t] = new se(t, 1, !1, e, "http://www.w3.org/XML/1998/namespace", !1, !1);
});
["tabIndex", "crossOrigin"].forEach(function (e) {
  te[e] = new se(e, 1, !1, e.toLowerCase(), null, !1, !1);
});
te.xlinkHref = new se(
  "xlinkHref",
  1,
  !1,
  "xlink:href",
  "http://www.w3.org/1999/xlink",
  !0,
  !1
);
["src", "href", "action", "formAction"].forEach(function (e) {
  te[e] = new se(e, 1, !1, e.toLowerCase(), null, !0, !0);
});
function mi(e, t, n, r) {
  var l = te.hasOwnProperty(t) ? te[t] : null;
  (l !== null
    ? l.type !== 0
    : r ||
      !(2 < t.length) ||
      (t[0] !== "o" && t[0] !== "O") ||
      (t[1] !== "n" && t[1] !== "N")) &&
    (Ff(t, n, l, r) && (n = null),
    r || l === null
      ? If(t) && (n === null ? e.removeAttribute(t) : e.setAttribute(t, "" + n))
      : l.mustUseProperty
      ? (e[l.propertyName] = n === null ? (l.type === 3 ? !1 : "") : n)
      : ((t = l.attributeName),
        (r = l.attributeNamespace),
        n === null
          ? e.removeAttribute(t)
          : ((l = l.type),
            (n = l === 3 || (l === 4 && n === !0) ? "" : "" + n),
            r ? e.setAttributeNS(r, t, n) : e.setAttribute(t, n))));
}
var be = zf.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED,
  yr = Symbol.for("react.element"),
  At = Symbol.for("react.portal"),
  Bt = Symbol.for("react.fragment"),
  yi = Symbol.for("react.strict_mode"),
  mu = Symbol.for("react.profiler"),
  Wa = Symbol.for("react.provider"),
  Qa = Symbol.for("react.context"),
  vi = Symbol.for("react.forward_ref"),
  yu = Symbol.for("react.suspense"),
  vu = Symbol.for("react.suspense_list"),
  gi = Symbol.for("react.memo"),
  nt = Symbol.for("react.lazy"),
  Ka = Symbol.for("react.offscreen"),
  vo = Symbol.iterator;
function wn(e) {
  return e === null || typeof e != "object"
    ? null
    : ((e = (vo && e[vo]) || e["@@iterator"]),
      typeof e == "function" ? e : null);
}
var V = Object.assign,
  Al;
function Ln(e) {
  if (Al === void 0)
    try {
      throw Error();
    } catch (n) {
      var t = n.stack.trim().match(/\n( *(at )?)/);
      Al = (t && t[1]) || "";
    }
  return (
    `
` +
    Al +
    e
  );
}
var Bl = !1;
function Hl(e, t) {
  if (!e || Bl) return "";
  Bl = !0;
  var n = Error.prepareStackTrace;
  Error.prepareStackTrace = void 0;
  try {
    if (t)
      if (
        ((t = function () {
          throw Error();
        }),
        Object.defineProperty(t.prototype, "props", {
          set: function () {
            throw Error();
          },
        }),
        typeof Reflect == "object" && Reflect.construct)
      ) {
        try {
          Reflect.construct(t, []);
        } catch (s) {
          var r = s;
        }
        Reflect.construct(e, [], t);
      } else {
        try {
          t.call();
        } catch (s) {
          r = s;
        }
        e.call(t.prototype);
      }
    else {
      try {
        throw Error();
      } catch (s) {
        r = s;
      }
      e();
    }
  } catch (s) {
    if (s && r && typeof s.stack == "string") {
      for (
        var l = s.stack.split(`
`),
          u = r.stack.split(`
`),
          i = l.length - 1,
          o = u.length - 1;
        1 <= i && 0 <= o && l[i] !== u[o];

      )
        o--;
      for (; 1 <= i && 0 <= o; i--, o--)
        if (l[i] !== u[o]) {
          if (i !== 1 || o !== 1)
            do
              if ((i--, o--, 0 > o || l[i] !== u[o])) {
                var a =
                  `
` + l[i].replace(" at new ", " at ");
                return (
                  e.displayName &&
                    a.includes("<anonymous>") &&
                    (a = a.replace("<anonymous>", e.displayName)),
                  a
                );
              }
            while (1 <= i && 0 <= o);
          break;
        }
    }
  } finally {
    (Bl = !1), (Error.prepareStackTrace = n);
  }
  return (e = e ? e.displayName || e.name : "") ? Ln(e) : "";
}
function Mf(e) {
  switch (e.tag) {
    case 5:
      return Ln(e.type);
    case 16:
      return Ln("Lazy");
    case 13:
      return Ln("Suspense");
    case 19:
      return Ln("SuspenseList");
    case 0:
    case 2:
    case 15:
      return (e = Hl(e.type, !1)), e;
    case 11:
      return (e = Hl(e.type.render, !1)), e;
    case 1:
      return (e = Hl(e.type, !0)), e;
    default:
      return "";
  }
}
function gu(e) {
  if (e == null) return null;
  if (typeof e == "function") return e.displayName || e.name || null;
  if (typeof e == "string") return e;
  switch (e) {
    case Bt:
      return "Fragment";
    case At:
      return "Portal";
    case mu:
      return "Profiler";
    case yi:
      return "StrictMode";
    case yu:
      return "Suspense";
    case vu:
      return "SuspenseList";
  }
  if (typeof e == "object")
    switch (e.$$typeof) {
      case Qa:
        return (e.displayName || "Context") + ".Consumer";
      case Wa:
        return (e._context.displayName || "Context") + ".Provider";
      case vi:
        var t = e.render;
        return (
          (e = e.displayName),
          e ||
            ((e = t.displayName || t.name || ""),
            (e = e !== "" ? "ForwardRef(" + e + ")" : "ForwardRef")),
          e
        );
      case gi:
        return (
          (t = e.displayName || null), t !== null ? t : gu(e.type) || "Memo"
        );
      case nt:
        (t = e._payload), (e = e._init);
        try {
          return gu(e(t));
        } catch {}
    }
  return null;
}
function $f(e) {
  var t = e.type;
  switch (e.tag) {
    case 24:
      return "Cache";
    case 9:
      return (t.displayName || "Context") + ".Consumer";
    case 10:
      return (t._context.displayName || "Context") + ".Provider";
    case 18:
      return "DehydratedFragment";
    case 11:
      return (
        (e = t.render),
        (e = e.displayName || e.name || ""),
        t.displayName || (e !== "" ? "ForwardRef(" + e + ")" : "ForwardRef")
      );
    case 7:
      return "Fragment";
    case 5:
      return t;
    case 4:
      return "Portal";
    case 3:
      return "Root";
    case 6:
      return "Text";
    case 16:
      return gu(t);
    case 8:
      return t === yi ? "StrictMode" : "Mode";
    case 22:
      return "Offscreen";
    case 12:
      return "Profiler";
    case 21:
      return "Scope";
    case 13:
      return "Suspense";
    case 19:
      return "SuspenseList";
    case 25:
      return "TracingMarker";
    case 1:
    case 0:
    case 17:
    case 2:
    case 14:
    case 15:
      if (typeof t == "function") return t.displayName || t.name || null;
      if (typeof t == "string") return t;
  }
  return null;
}
function yt(e) {
  switch (typeof e) {
    case "boolean":
    case "number":
    case "string":
    case "undefined":
      return e;
    case "object":
      return e;
    default:
      return "";
  }
}
function Ya(e) {
  var t = e.type;
  return (
    (e = e.nodeName) &&
    e.toLowerCase() === "input" &&
    (t === "checkbox" || t === "radio")
  );
}
function jf(e) {
  var t = Ya(e) ? "checked" : "value",
    n = Object.getOwnPropertyDescriptor(e.constructor.prototype, t),
    r = "" + e[t];
  if (
    !e.hasOwnProperty(t) &&
    typeof n < "u" &&
    typeof n.get == "function" &&
    typeof n.set == "function"
  ) {
    var l = n.get,
      u = n.set;
    return (
      Object.defineProperty(e, t, {
        configurable: !0,
        get: function () {
          return l.call(this);
        },
        set: function (i) {
          (r = "" + i), u.call(this, i);
        },
      }),
      Object.defineProperty(e, t, { enumerable: n.enumerable }),
      {
        getValue: function () {
          return r;
        },
        setValue: function (i) {
          r = "" + i;
        },
        stopTracking: function () {
          (e._valueTracker = null), delete e[t];
        },
      }
    );
  }
}
function vr(e) {
  e._valueTracker || (e._valueTracker = jf(e));
}
function Xa(e) {
  if (!e) return !1;
  var t = e._valueTracker;
  if (!t) return !0;
  var n = t.getValue(),
    r = "";
  return (
    e && (r = Ya(e) ? (e.checked ? "true" : "false") : e.value),
    (e = r),
    e !== n ? (t.setValue(e), !0) : !1
  );
}
function Yr(e) {
  if (((e = e || (typeof document < "u" ? document : void 0)), typeof e > "u"))
    return null;
  try {
    return e.activeElement || e.body;
  } catch {
    return e.body;
  }
}
function wu(e, t) {
  var n = t.checked;
  return V({}, t, {
    defaultChecked: void 0,
    defaultValue: void 0,
    value: void 0,
    checked: n ?? e._wrapperState.initialChecked,
  });
}
function go(e, t) {
  var n = t.defaultValue == null ? "" : t.defaultValue,
    r = t.checked != null ? t.checked : t.defaultChecked;
  (n = yt(t.value != null ? t.value : n)),
    (e._wrapperState = {
      initialChecked: r,
      initialValue: n,
      controlled:
        t.type === "checkbox" || t.type === "radio"
          ? t.checked != null
          : t.value != null,
    });
}
function Ga(e, t) {
  (t = t.checked), t != null && mi(e, "checked", t, !1);
}
function Su(e, t) {
  Ga(e, t);
  var n = yt(t.value),
    r = t.type;
  if (n != null)
    r === "number"
      ? ((n === 0 && e.value === "") || e.value != n) && (e.value = "" + n)
      : e.value !== "" + n && (e.value = "" + n);
  else if (r === "submit" || r === "reset") {
    e.removeAttribute("value");
    return;
  }
  t.hasOwnProperty("value")
    ? ku(e, t.type, n)
    : t.hasOwnProperty("defaultValue") && ku(e, t.type, yt(t.defaultValue)),
    t.checked == null &&
      t.defaultChecked != null &&
      (e.defaultChecked = !!t.defaultChecked);
}
function wo(e, t, n) {
  if (t.hasOwnProperty("value") || t.hasOwnProperty("defaultValue")) {
    var r = t.type;
    if (
      !(
        (r !== "submit" && r !== "reset") ||
        (t.value !== void 0 && t.value !== null)
      )
    )
      return;
    (t = "" + e._wrapperState.initialValue),
      n || t === e.value || (e.value = t),
      (e.defaultValue = t);
  }
  (n = e.name),
    n !== "" && (e.name = ""),
    (e.defaultChecked = !!e._wrapperState.initialChecked),
    n !== "" && (e.name = n);
}
function ku(e, t, n) {
  (t !== "number" || Yr(e.ownerDocument) !== e) &&
    (n == null
      ? (e.defaultValue = "" + e._wrapperState.initialValue)
      : e.defaultValue !== "" + n && (e.defaultValue = "" + n));
}
var Nn = Array.isArray;
function qt(e, t, n, r) {
  if (((e = e.options), t)) {
    t = {};
    for (var l = 0; l < n.length; l++) t["$" + n[l]] = !0;
    for (n = 0; n < e.length; n++)
      (l = t.hasOwnProperty("$" + e[n].value)),
        e[n].selected !== l && (e[n].selected = l),
        l && r && (e[n].defaultSelected = !0);
  } else {
    for (n = "" + yt(n), t = null, l = 0; l < e.length; l++) {
      if (e[l].value === n) {
        (e[l].selected = !0), r && (e[l].defaultSelected = !0);
        return;
      }
      t !== null || e[l].disabled || (t = e[l]);
    }
    t !== null && (t.selected = !0);
  }
}
function Eu(e, t) {
  if (t.dangerouslySetInnerHTML != null) throw Error(k(91));
  return V({}, t, {
    value: void 0,
    defaultValue: void 0,
    children: "" + e._wrapperState.initialValue,
  });
}
function So(e, t) {
  var n = t.value;
  if (n == null) {
    if (((n = t.children), (t = t.defaultValue), n != null)) {
      if (t != null) throw Error(k(92));
      if (Nn(n)) {
        if (1 < n.length) throw Error(k(93));
        n = n[0];
      }
      t = n;
    }
    t == null && (t = ""), (n = t);
  }
  e._wrapperState = { initialValue: yt(n) };
}
function Za(e, t) {
  var n = yt(t.value),
    r = yt(t.defaultValue);
  n != null &&
    ((n = "" + n),
    n !== e.value && (e.value = n),
    t.defaultValue == null && e.defaultValue !== n && (e.defaultValue = n)),
    r != null && (e.defaultValue = "" + r);
}
function ko(e) {
  var t = e.textContent;
  t === e._wrapperState.initialValue && t !== "" && t !== null && (e.value = t);
}
function Ja(e) {
  switch (e) {
    case "svg":
      return "http://www.w3.org/2000/svg";
    case "math":
      return "http://www.w3.org/1998/Math/MathML";
    default:
      return "http://www.w3.org/1999/xhtml";
  }
}
function xu(e, t) {
  return e == null || e === "http://www.w3.org/1999/xhtml"
    ? Ja(t)
    : e === "http://www.w3.org/2000/svg" && t === "foreignObject"
    ? "http://www.w3.org/1999/xhtml"
    : e;
}
var gr,
  qa = (function (e) {
    return typeof MSApp < "u" && MSApp.execUnsafeLocalFunction
      ? function (t, n, r, l) {
          MSApp.execUnsafeLocalFunction(function () {
            return e(t, n, r, l);
          });
        }
      : e;
  })(function (e, t) {
    if (e.namespaceURI !== "http://www.w3.org/2000/svg" || "innerHTML" in e)
      e.innerHTML = t;
    else {
      for (
        gr = gr || document.createElement("div"),
          gr.innerHTML = "<svg>" + t.valueOf().toString() + "</svg>",
          t = gr.firstChild;
        e.firstChild;

      )
        e.removeChild(e.firstChild);
      for (; t.firstChild; ) e.appendChild(t.firstChild);
    }
  });
function Hn(e, t) {
  if (t) {
    var n = e.firstChild;
    if (n && n === e.lastChild && n.nodeType === 3) {
      n.nodeValue = t;
      return;
    }
  }
  e.textContent = t;
}
var On = {
    animationIterationCount: !0,
    aspectRatio: !0,
    borderImageOutset: !0,
    borderImageSlice: !0,
    borderImageWidth: !0,
    boxFlex: !0,
    boxFlexGroup: !0,
    boxOrdinalGroup: !0,
    columnCount: !0,
    columns: !0,
    flex: !0,
    flexGrow: !0,
    flexPositive: !0,
    flexShrink: !0,
    flexNegative: !0,
    flexOrder: !0,
    gridArea: !0,
    gridRow: !0,
    gridRowEnd: !0,
    gridRowSpan: !0,
    gridRowStart: !0,
    gridColumn: !0,
    gridColumnEnd: !0,
    gridColumnSpan: !0,
    gridColumnStart: !0,
    fontWeight: !0,
    lineClamp: !0,
    lineHeight: !0,
    opacity: !0,
    order: !0,
    orphans: !0,
    tabSize: !0,
    widows: !0,
    zIndex: !0,
    zoom: !0,
    fillOpacity: !0,
    floodOpacity: !0,
    stopOpacity: !0,
    strokeDasharray: !0,
    strokeDashoffset: !0,
    strokeMiterlimit: !0,
    strokeOpacity: !0,
    strokeWidth: !0,
  },
  Uf = ["Webkit", "ms", "Moz", "O"];
Object.keys(On).forEach(function (e) {
  Uf.forEach(function (t) {
    (t = t + e.charAt(0).toUpperCase() + e.substring(1)), (On[t] = On[e]);
  });
});
function ba(e, t, n) {
  return t == null || typeof t == "boolean" || t === ""
    ? ""
    : n || typeof t != "number" || t === 0 || (On.hasOwnProperty(e) && On[e])
    ? ("" + t).trim()
    : t + "px";
}
function es(e, t) {
  e = e.style;
  for (var n in t)
    if (t.hasOwnProperty(n)) {
      var r = n.indexOf("--") === 0,
        l = ba(n, t[n], r);
      n === "float" && (n = "cssFloat"), r ? e.setProperty(n, l) : (e[n] = l);
    }
}
var Af = V(
  { menuitem: !0 },
  {
    area: !0,
    base: !0,
    br: !0,
    col: !0,
    embed: !0,
    hr: !0,
    img: !0,
    input: !0,
    keygen: !0,
    link: !0,
    meta: !0,
    param: !0,
    source: !0,
    track: !0,
    wbr: !0,
  }
);
function Cu(e, t) {
  if (t) {
    if (Af[e] && (t.children != null || t.dangerouslySetInnerHTML != null))
      throw Error(k(137, e));
    if (t.dangerouslySetInnerHTML != null) {
      if (t.children != null) throw Error(k(60));
      if (
        typeof t.dangerouslySetInnerHTML != "object" ||
        !("__html" in t.dangerouslySetInnerHTML)
      )
        throw Error(k(61));
    }
    if (t.style != null && typeof t.style != "object") throw Error(k(62));
  }
}
function Pu(e, t) {
  if (e.indexOf("-") === -1) return typeof t.is == "string";
  switch (e) {
    case "annotation-xml":
    case "color-profile":
    case "font-face":
    case "font-face-src":
    case "font-face-uri":
    case "font-face-format":
    case "font-face-name":
    case "missing-glyph":
      return !1;
    default:
      return !0;
  }
}
var _u = null;
function wi(e) {
  return (
    (e = e.target || e.srcElement || window),
    e.correspondingUseElement && (e = e.correspondingUseElement),
    e.nodeType === 3 ? e.parentNode : e
  );
}
var Ru = null,
  bt = null,
  en = null;
function Eo(e) {
  if ((e = ar(e))) {
    if (typeof Ru != "function") throw Error(k(280));
    var t = e.stateNode;
    t && ((t = xl(t)), Ru(e.stateNode, e.type, t));
  }
}
function ts(e) {
  bt ? (en ? en.push(e) : (en = [e])) : (bt = e);
}
function ns() {
  if (bt) {
    var e = bt,
      t = en;
    if (((en = bt = null), Eo(e), t)) for (e = 0; e < t.length; e++) Eo(t[e]);
  }
}
function rs(e, t) {
  return e(t);
}
function ls() {}
var Vl = !1;
function us(e, t, n) {
  if (Vl) return e(t, n);
  Vl = !0;
  try {
    return rs(e, t, n);
  } finally {
    (Vl = !1), (bt !== null || en !== null) && (ls(), ns());
  }
}
function Vn(e, t) {
  var n = e.stateNode;
  if (n === null) return null;
  var r = xl(n);
  if (r === null) return null;
  n = r[t];
  e: switch (t) {
    case "onClick":
    case "onClickCapture":
    case "onDoubleClick":
    case "onDoubleClickCapture":
    case "onMouseDown":
    case "onMouseDownCapture":
    case "onMouseMove":
    case "onMouseMoveCapture":
    case "onMouseUp":
    case "onMouseUpCapture":
    case "onMouseEnter":
      (r = !r.disabled) ||
        ((e = e.type),
        (r = !(
          e === "button" ||
          e === "input" ||
          e === "select" ||
          e === "textarea"
        ))),
        (e = !r);
      break e;
    default:
      e = !1;
  }
  if (e) return null;
  if (n && typeof n != "function") throw Error(k(231, t, typeof n));
  return n;
}
var Lu = !1;
if (Ge)
  try {
    var Sn = {};
    Object.defineProperty(Sn, "passive", {
      get: function () {
        Lu = !0;
      },
    }),
      window.addEventListener("test", Sn, Sn),
      window.removeEventListener("test", Sn, Sn);
  } catch {
    Lu = !1;
  }
function Bf(e, t, n, r, l, u, i, o, a) {
  var s = Array.prototype.slice.call(arguments, 3);
  try {
    t.apply(n, s);
  } catch (h) {
    this.onError(h);
  }
}
var In = !1,
  Xr = null,
  Gr = !1,
  Nu = null,
  Hf = {
    onError: function (e) {
      (In = !0), (Xr = e);
    },
  };
function Vf(e, t, n, r, l, u, i, o, a) {
  (In = !1), (Xr = null), Bf.apply(Hf, arguments);
}
function Wf(e, t, n, r, l, u, i, o, a) {
  if ((Vf.apply(this, arguments), In)) {
    if (In) {
      var s = Xr;
      (In = !1), (Xr = null);
    } else throw Error(k(198));
    Gr || ((Gr = !0), (Nu = s));
  }
}
function jt(e) {
  var t = e,
    n = e;
  if (e.alternate) for (; t.return; ) t = t.return;
  else {
    e = t;
    do (t = e), t.flags & 4098 && (n = t.return), (e = t.return);
    while (e);
  }
  return t.tag === 3 ? n : null;
}
function is(e) {
  if (e.tag === 13) {
    var t = e.memoizedState;
    if (
      (t === null && ((e = e.alternate), e !== null && (t = e.memoizedState)),
      t !== null)
    )
      return t.dehydrated;
  }
  return null;
}
function xo(e) {
  if (jt(e) !== e) throw Error(k(188));
}
function Qf(e) {
  var t = e.alternate;
  if (!t) {
    if (((t = jt(e)), t === null)) throw Error(k(188));
    return t !== e ? null : e;
  }
  for (var n = e, r = t; ; ) {
    var l = n.return;
    if (l === null) break;
    var u = l.alternate;
    if (u === null) {
      if (((r = l.return), r !== null)) {
        n = r;
        continue;
      }
      break;
    }
    if (l.child === u.child) {
      for (u = l.child; u; ) {
        if (u === n) return xo(l), e;
        if (u === r) return xo(l), t;
        u = u.sibling;
      }
      throw Error(k(188));
    }
    if (n.return !== r.return) (n = l), (r = u);
    else {
      for (var i = !1, o = l.child; o; ) {
        if (o === n) {
          (i = !0), (n = l), (r = u);
          break;
        }
        if (o === r) {
          (i = !0), (r = l), (n = u);
          break;
        }
        o = o.sibling;
      }
      if (!i) {
        for (o = u.child; o; ) {
          if (o === n) {
            (i = !0), (n = u), (r = l);
            break;
          }
          if (o === r) {
            (i = !0), (r = u), (n = l);
            break;
          }
          o = o.sibling;
        }
        if (!i) throw Error(k(189));
      }
    }
    if (n.alternate !== r) throw Error(k(190));
  }
  if (n.tag !== 3) throw Error(k(188));
  return n.stateNode.current === n ? e : t;
}
function os(e) {
  return (e = Qf(e)), e !== null ? as(e) : null;
}
function as(e) {
  if (e.tag === 5 || e.tag === 6) return e;
  for (e = e.child; e !== null; ) {
    var t = as(e);
    if (t !== null) return t;
    e = e.sibling;
  }
  return null;
}
var ss = we.unstable_scheduleCallback,
  Co = we.unstable_cancelCallback,
  Kf = we.unstable_shouldYield,
  Yf = we.unstable_requestPaint,
  K = we.unstable_now,
  Xf = we.unstable_getCurrentPriorityLevel,
  Si = we.unstable_ImmediatePriority,
  cs = we.unstable_UserBlockingPriority,
  Zr = we.unstable_NormalPriority,
  Gf = we.unstable_LowPriority,
  fs = we.unstable_IdlePriority,
  wl = null,
  Ue = null;
function Zf(e) {
  if (Ue && typeof Ue.onCommitFiberRoot == "function")
    try {
      Ue.onCommitFiberRoot(wl, e, void 0, (e.current.flags & 128) === 128);
    } catch {}
}
var Oe = Math.clz32 ? Math.clz32 : bf,
  Jf = Math.log,
  qf = Math.LN2;
function bf(e) {
  return (e >>>= 0), e === 0 ? 32 : (31 - ((Jf(e) / qf) | 0)) | 0;
}
var wr = 64,
  Sr = 4194304;
function Tn(e) {
  switch (e & -e) {
    case 1:
      return 1;
    case 2:
      return 2;
    case 4:
      return 4;
    case 8:
      return 8;
    case 16:
      return 16;
    case 32:
      return 32;
    case 64:
    case 128:
    case 256:
    case 512:
    case 1024:
    case 2048:
    case 4096:
    case 8192:
    case 16384:
    case 32768:
    case 65536:
    case 131072:
    case 262144:
    case 524288:
    case 1048576:
    case 2097152:
      return e & 4194240;
    case 4194304:
    case 8388608:
    case 16777216:
    case 33554432:
    case 67108864:
      return e & 130023424;
    case 134217728:
      return 134217728;
    case 268435456:
      return 268435456;
    case 536870912:
      return 536870912;
    case 1073741824:
      return 1073741824;
    default:
      return e;
  }
}
function Jr(e, t) {
  var n = e.pendingLanes;
  if (n === 0) return 0;
  var r = 0,
    l = e.suspendedLanes,
    u = e.pingedLanes,
    i = n & 268435455;
  if (i !== 0) {
    var o = i & ~l;
    o !== 0 ? (r = Tn(o)) : ((u &= i), u !== 0 && (r = Tn(u)));
  } else (i = n & ~l), i !== 0 ? (r = Tn(i)) : u !== 0 && (r = Tn(u));
  if (r === 0) return 0;
  if (
    t !== 0 &&
    t !== r &&
    !(t & l) &&
    ((l = r & -r), (u = t & -t), l >= u || (l === 16 && (u & 4194240) !== 0))
  )
    return t;
  if ((r & 4 && (r |= n & 16), (t = e.entangledLanes), t !== 0))
    for (e = e.entanglements, t &= r; 0 < t; )
      (n = 31 - Oe(t)), (l = 1 << n), (r |= e[n]), (t &= ~l);
  return r;
}
function ed(e, t) {
  switch (e) {
    case 1:
    case 2:
    case 4:
      return t + 250;
    case 8:
    case 16:
    case 32:
    case 64:
    case 128:
    case 256:
    case 512:
    case 1024:
    case 2048:
    case 4096:
    case 8192:
    case 16384:
    case 32768:
    case 65536:
    case 131072:
    case 262144:
    case 524288:
    case 1048576:
    case 2097152:
      return t + 5e3;
    case 4194304:
    case 8388608:
    case 16777216:
    case 33554432:
    case 67108864:
      return -1;
    case 134217728:
    case 268435456:
    case 536870912:
    case 1073741824:
      return -1;
    default:
      return -1;
  }
}
function td(e, t) {
  for (
    var n = e.suspendedLanes,
      r = e.pingedLanes,
      l = e.expirationTimes,
      u = e.pendingLanes;
    0 < u;

  ) {
    var i = 31 - Oe(u),
      o = 1 << i,
      a = l[i];
    a === -1
      ? (!(o & n) || o & r) && (l[i] = ed(o, t))
      : a <= t && (e.expiredLanes |= o),
      (u &= ~o);
  }
}
function Tu(e) {
  return (
    (e = e.pendingLanes & -1073741825),
    e !== 0 ? e : e & 1073741824 ? 1073741824 : 0
  );
}
function ds() {
  var e = wr;
  return (wr <<= 1), !(wr & 4194240) && (wr = 64), e;
}
function Wl(e) {
  for (var t = [], n = 0; 31 > n; n++) t.push(e);
  return t;
}
function ir(e, t, n) {
  (e.pendingLanes |= t),
    t !== 536870912 && ((e.suspendedLanes = 0), (e.pingedLanes = 0)),
    (e = e.eventTimes),
    (t = 31 - Oe(t)),
    (e[t] = n);
}
function nd(e, t) {
  var n = e.pendingLanes & ~t;
  (e.pendingLanes = t),
    (e.suspendedLanes = 0),
    (e.pingedLanes = 0),
    (e.expiredLanes &= t),
    (e.mutableReadLanes &= t),
    (e.entangledLanes &= t),
    (t = e.entanglements);
  var r = e.eventTimes;
  for (e = e.expirationTimes; 0 < n; ) {
    var l = 31 - Oe(n),
      u = 1 << l;
    (t[l] = 0), (r[l] = -1), (e[l] = -1), (n &= ~u);
  }
}
function ki(e, t) {
  var n = (e.entangledLanes |= t);
  for (e = e.entanglements; n; ) {
    var r = 31 - Oe(n),
      l = 1 << r;
    (l & t) | (e[r] & t) && (e[r] |= t), (n &= ~l);
  }
}
var F = 0;
function ps(e) {
  return (e &= -e), 1 < e ? (4 < e ? (e & 268435455 ? 16 : 536870912) : 4) : 1;
}
var hs,
  Ei,
  ms,
  ys,
  vs,
  zu = !1,
  kr = [],
  at = null,
  st = null,
  ct = null,
  Wn = new Map(),
  Qn = new Map(),
  lt = [],
  rd =
    "mousedown mouseup touchcancel touchend touchstart auxclick dblclick pointercancel pointerdown pointerup dragend dragstart drop compositionend compositionstart keydown keypress keyup input textInput copy cut paste click change contextmenu reset submit".split(
      " "
    );
function Po(e, t) {
  switch (e) {
    case "focusin":
    case "focusout":
      at = null;
      break;
    case "dragenter":
    case "dragleave":
      st = null;
      break;
    case "mouseover":
    case "mouseout":
      ct = null;
      break;
    case "pointerover":
    case "pointerout":
      Wn.delete(t.pointerId);
      break;
    case "gotpointercapture":
    case "lostpointercapture":
      Qn.delete(t.pointerId);
  }
}
function kn(e, t, n, r, l, u) {
  return e === null || e.nativeEvent !== u
    ? ((e = {
        blockedOn: t,
        domEventName: n,
        eventSystemFlags: r,
        nativeEvent: u,
        targetContainers: [l],
      }),
      t !== null && ((t = ar(t)), t !== null && Ei(t)),
      e)
    : ((e.eventSystemFlags |= r),
      (t = e.targetContainers),
      l !== null && t.indexOf(l) === -1 && t.push(l),
      e);
}
function ld(e, t, n, r, l) {
  switch (t) {
    case "focusin":
      return (at = kn(at, e, t, n, r, l)), !0;
    case "dragenter":
      return (st = kn(st, e, t, n, r, l)), !0;
    case "mouseover":
      return (ct = kn(ct, e, t, n, r, l)), !0;
    case "pointerover":
      var u = l.pointerId;
      return Wn.set(u, kn(Wn.get(u) || null, e, t, n, r, l)), !0;
    case "gotpointercapture":
      return (
        (u = l.pointerId), Qn.set(u, kn(Qn.get(u) || null, e, t, n, r, l)), !0
      );
  }
  return !1;
}
function gs(e) {
  var t = Rt(e.target);
  if (t !== null) {
    var n = jt(t);
    if (n !== null) {
      if (((t = n.tag), t === 13)) {
        if (((t = is(n)), t !== null)) {
          (e.blockedOn = t),
            vs(e.priority, function () {
              ms(n);
            });
          return;
        }
      } else if (t === 3 && n.stateNode.current.memoizedState.isDehydrated) {
        e.blockedOn = n.tag === 3 ? n.stateNode.containerInfo : null;
        return;
      }
    }
  }
  e.blockedOn = null;
}
function Fr(e) {
  if (e.blockedOn !== null) return !1;
  for (var t = e.targetContainers; 0 < t.length; ) {
    var n = Ou(e.domEventName, e.eventSystemFlags, t[0], e.nativeEvent);
    if (n === null) {
      n = e.nativeEvent;
      var r = new n.constructor(n.type, n);
      (_u = r), n.target.dispatchEvent(r), (_u = null);
    } else return (t = ar(n)), t !== null && Ei(t), (e.blockedOn = n), !1;
    t.shift();
  }
  return !0;
}
function _o(e, t, n) {
  Fr(e) && n.delete(t);
}
function ud() {
  (zu = !1),
    at !== null && Fr(at) && (at = null),
    st !== null && Fr(st) && (st = null),
    ct !== null && Fr(ct) && (ct = null),
    Wn.forEach(_o),
    Qn.forEach(_o);
}
function En(e, t) {
  e.blockedOn === t &&
    ((e.blockedOn = null),
    zu ||
      ((zu = !0),
      we.unstable_scheduleCallback(we.unstable_NormalPriority, ud)));
}
function Kn(e) {
  function t(l) {
    return En(l, e);
  }
  if (0 < kr.length) {
    En(kr[0], e);
    for (var n = 1; n < kr.length; n++) {
      var r = kr[n];
      r.blockedOn === e && (r.blockedOn = null);
    }
  }
  for (
    at !== null && En(at, e),
      st !== null && En(st, e),
      ct !== null && En(ct, e),
      Wn.forEach(t),
      Qn.forEach(t),
      n = 0;
    n < lt.length;
    n++
  )
    (r = lt[n]), r.blockedOn === e && (r.blockedOn = null);
  for (; 0 < lt.length && ((n = lt[0]), n.blockedOn === null); )
    gs(n), n.blockedOn === null && lt.shift();
}
var tn = be.ReactCurrentBatchConfig,
  qr = !0;
function id(e, t, n, r) {
  var l = F,
    u = tn.transition;
  tn.transition = null;
  try {
    (F = 1), xi(e, t, n, r);
  } finally {
    (F = l), (tn.transition = u);
  }
}
function od(e, t, n, r) {
  var l = F,
    u = tn.transition;
  tn.transition = null;
  try {
    (F = 4), xi(e, t, n, r);
  } finally {
    (F = l), (tn.transition = u);
  }
}
function xi(e, t, n, r) {
  if (qr) {
    var l = Ou(e, t, n, r);
    if (l === null) eu(e, t, r, br, n), Po(e, r);
    else if (ld(l, e, t, n, r)) r.stopPropagation();
    else if ((Po(e, r), t & 4 && -1 < rd.indexOf(e))) {
      for (; l !== null; ) {
        var u = ar(l);
        if (
          (u !== null && hs(u),
          (u = Ou(e, t, n, r)),
          u === null && eu(e, t, r, br, n),
          u === l)
        )
          break;
        l = u;
      }
      l !== null && r.stopPropagation();
    } else eu(e, t, r, null, n);
  }
}
var br = null;
function Ou(e, t, n, r) {
  if (((br = null), (e = wi(r)), (e = Rt(e)), e !== null))
    if (((t = jt(e)), t === null)) e = null;
    else if (((n = t.tag), n === 13)) {
      if (((e = is(t)), e !== null)) return e;
      e = null;
    } else if (n === 3) {
      if (t.stateNode.current.memoizedState.isDehydrated)
        return t.tag === 3 ? t.stateNode.containerInfo : null;
      e = null;
    } else t !== e && (e = null);
  return (br = e), null;
}
function ws(e) {
  switch (e) {
    case "cancel":
    case "click":
    case "close":
    case "contextmenu":
    case "copy":
    case "cut":
    case "auxclick":
    case "dblclick":
    case "dragend":
    case "dragstart":
    case "drop":
    case "focusin":
    case "focusout":
    case "input":
    case "invalid":
    case "keydown":
    case "keypress":
    case "keyup":
    case "mousedown":
    case "mouseup":
    case "paste":
    case "pause":
    case "play":
    case "pointercancel":
    case "pointerdown":
    case "pointerup":
    case "ratechange":
    case "reset":
    case "resize":
    case "seeked":
    case "submit":
    case "touchcancel":
    case "touchend":
    case "touchstart":
    case "volumechange":
    case "change":
    case "selectionchange":
    case "textInput":
    case "compositionstart":
    case "compositionend":
    case "compositionupdate":
    case "beforeblur":
    case "afterblur":
    case "beforeinput":
    case "blur":
    case "fullscreenchange":
    case "focus":
    case "hashchange":
    case "popstate":
    case "select":
    case "selectstart":
      return 1;
    case "drag":
    case "dragenter":
    case "dragexit":
    case "dragleave":
    case "dragover":
    case "mousemove":
    case "mouseout":
    case "mouseover":
    case "pointermove":
    case "pointerout":
    case "pointerover":
    case "scroll":
    case "toggle":
    case "touchmove":
    case "wheel":
    case "mouseenter":
    case "mouseleave":
    case "pointerenter":
    case "pointerleave":
      return 4;
    case "message":
      switch (Xf()) {
        case Si:
          return 1;
        case cs:
          return 4;
        case Zr:
        case Gf:
          return 16;
        case fs:
          return 536870912;
        default:
          return 16;
      }
    default:
      return 16;
  }
}
var it = null,
  Ci = null,
  Mr = null;
function Ss() {
  if (Mr) return Mr;
  var e,
    t = Ci,
    n = t.length,
    r,
    l = "value" in it ? it.value : it.textContent,
    u = l.length;
  for (e = 0; e < n && t[e] === l[e]; e++);
  var i = n - e;
  for (r = 1; r <= i && t[n - r] === l[u - r]; r++);
  return (Mr = l.slice(e, 1 < r ? 1 - r : void 0));
}
function $r(e) {
  var t = e.keyCode;
  return (
    "charCode" in e
      ? ((e = e.charCode), e === 0 && t === 13 && (e = 13))
      : (e = t),
    e === 10 && (e = 13),
    32 <= e || e === 13 ? e : 0
  );
}
function Er() {
  return !0;
}
function Ro() {
  return !1;
}
function ke(e) {
  function t(n, r, l, u, i) {
    (this._reactName = n),
      (this._targetInst = l),
      (this.type = r),
      (this.nativeEvent = u),
      (this.target = i),
      (this.currentTarget = null);
    for (var o in e)
      e.hasOwnProperty(o) && ((n = e[o]), (this[o] = n ? n(u) : u[o]));
    return (
      (this.isDefaultPrevented = (
        u.defaultPrevented != null ? u.defaultPrevented : u.returnValue === !1
      )
        ? Er
        : Ro),
      (this.isPropagationStopped = Ro),
      this
    );
  }
  return (
    V(t.prototype, {
      preventDefault: function () {
        this.defaultPrevented = !0;
        var n = this.nativeEvent;
        n &&
          (n.preventDefault
            ? n.preventDefault()
            : typeof n.returnValue != "unknown" && (n.returnValue = !1),
          (this.isDefaultPrevented = Er));
      },
      stopPropagation: function () {
        var n = this.nativeEvent;
        n &&
          (n.stopPropagation
            ? n.stopPropagation()
            : typeof n.cancelBubble != "unknown" && (n.cancelBubble = !0),
          (this.isPropagationStopped = Er));
      },
      persist: function () {},
      isPersistent: Er,
    }),
    t
  );
}
var pn = {
    eventPhase: 0,
    bubbles: 0,
    cancelable: 0,
    timeStamp: function (e) {
      return e.timeStamp || Date.now();
    },
    defaultPrevented: 0,
    isTrusted: 0,
  },
  Pi = ke(pn),
  or = V({}, pn, { view: 0, detail: 0 }),
  ad = ke(or),
  Ql,
  Kl,
  xn,
  Sl = V({}, or, {
    screenX: 0,
    screenY: 0,
    clientX: 0,
    clientY: 0,
    pageX: 0,
    pageY: 0,
    ctrlKey: 0,
    shiftKey: 0,
    altKey: 0,
    metaKey: 0,
    getModifierState: _i,
    button: 0,
    buttons: 0,
    relatedTarget: function (e) {
      return e.relatedTarget === void 0
        ? e.fromElement === e.srcElement
          ? e.toElement
          : e.fromElement
        : e.relatedTarget;
    },
    movementX: function (e) {
      return "movementX" in e
        ? e.movementX
        : (e !== xn &&
            (xn && e.type === "mousemove"
              ? ((Ql = e.screenX - xn.screenX), (Kl = e.screenY - xn.screenY))
              : (Kl = Ql = 0),
            (xn = e)),
          Ql);
    },
    movementY: function (e) {
      return "movementY" in e ? e.movementY : Kl;
    },
  }),
  Lo = ke(Sl),
  sd = V({}, Sl, { dataTransfer: 0 }),
  cd = ke(sd),
  fd = V({}, or, { relatedTarget: 0 }),
  Yl = ke(fd),
  dd = V({}, pn, { animationName: 0, elapsedTime: 0, pseudoElement: 0 }),
  pd = ke(dd),
  hd = V({}, pn, {
    clipboardData: function (e) {
      return "clipboardData" in e ? e.clipboardData : window.clipboardData;
    },
  }),
  md = ke(hd),
  yd = V({}, pn, { data: 0 }),
  No = ke(yd),
  vd = {
    Esc: "Escape",
    Spacebar: " ",
    Left: "ArrowLeft",
    Up: "ArrowUp",
    Right: "ArrowRight",
    Down: "ArrowDown",
    Del: "Delete",
    Win: "OS",
    Menu: "ContextMenu",
    Apps: "ContextMenu",
    Scroll: "ScrollLock",
    MozPrintableKey: "Unidentified",
  },
  gd = {
    8: "Backspace",
    9: "Tab",
    12: "Clear",
    13: "Enter",
    16: "Shift",
    17: "Control",
    18: "Alt",
    19: "Pause",
    20: "CapsLock",
    27: "Escape",
    32: " ",
    33: "PageUp",
    34: "PageDown",
    35: "End",
    36: "Home",
    37: "ArrowLeft",
    38: "ArrowUp",
    39: "ArrowRight",
    40: "ArrowDown",
    45: "Insert",
    46: "Delete",
    112: "F1",
    113: "F2",
    114: "F3",
    115: "F4",
    116: "F5",
    117: "F6",
    118: "F7",
    119: "F8",
    120: "F9",
    121: "F10",
    122: "F11",
    123: "F12",
    144: "NumLock",
    145: "ScrollLock",
    224: "Meta",
  },
  wd = {
    Alt: "altKey",
    Control: "ctrlKey",
    Meta: "metaKey",
    Shift: "shiftKey",
  };
function Sd(e) {
  var t = this.nativeEvent;
  return t.getModifierState ? t.getModifierState(e) : (e = wd[e]) ? !!t[e] : !1;
}
function _i() {
  return Sd;
}
var kd = V({}, or, {
    key: function (e) {
      if (e.key) {
        var t = vd[e.key] || e.key;
        if (t !== "Unidentified") return t;
      }
      return e.type === "keypress"
        ? ((e = $r(e)), e === 13 ? "Enter" : String.fromCharCode(e))
        : e.type === "keydown" || e.type === "keyup"
        ? gd[e.keyCode] || "Unidentified"
        : "";
    },
    code: 0,
    location: 0,
    ctrlKey: 0,
    shiftKey: 0,
    altKey: 0,
    metaKey: 0,
    repeat: 0,
    locale: 0,
    getModifierState: _i,
    charCode: function (e) {
      return e.type === "keypress" ? $r(e) : 0;
    },
    keyCode: function (e) {
      return e.type === "keydown" || e.type === "keyup" ? e.keyCode : 0;
    },
    which: function (e) {
      return e.type === "keypress"
        ? $r(e)
        : e.type === "keydown" || e.type === "keyup"
        ? e.keyCode
        : 0;
    },
  }),
  Ed = ke(kd),
  xd = V({}, Sl, {
    pointerId: 0,
    width: 0,
    height: 0,
    pressure: 0,
    tangentialPressure: 0,
    tiltX: 0,
    tiltY: 0,
    twist: 0,
    pointerType: 0,
    isPrimary: 0,
  }),
  To = ke(xd),
  Cd = V({}, or, {
    touches: 0,
    targetTouches: 0,
    changedTouches: 0,
    altKey: 0,
    metaKey: 0,
    ctrlKey: 0,
    shiftKey: 0,
    getModifierState: _i,
  }),
  Pd = ke(Cd),
  _d = V({}, pn, { propertyName: 0, elapsedTime: 0, pseudoElement: 0 }),
  Rd = ke(_d),
  Ld = V({}, Sl, {
    deltaX: function (e) {
      return "deltaX" in e ? e.deltaX : "wheelDeltaX" in e ? -e.wheelDeltaX : 0;
    },
    deltaY: function (e) {
      return "deltaY" in e
        ? e.deltaY
        : "wheelDeltaY" in e
        ? -e.wheelDeltaY
        : "wheelDelta" in e
        ? -e.wheelDelta
        : 0;
    },
    deltaZ: 0,
    deltaMode: 0,
  }),
  Nd = ke(Ld),
  Td = [9, 13, 27, 32],
  Ri = Ge && "CompositionEvent" in window,
  Dn = null;
Ge && "documentMode" in document && (Dn = document.documentMode);
var zd = Ge && "TextEvent" in window && !Dn,
  ks = Ge && (!Ri || (Dn && 8 < Dn && 11 >= Dn)),
  zo = " ",
  Oo = !1;
function Es(e, t) {
  switch (e) {
    case "keyup":
      return Td.indexOf(t.keyCode) !== -1;
    case "keydown":
      return t.keyCode !== 229;
    case "keypress":
    case "mousedown":
    case "focusout":
      return !0;
    default:
      return !1;
  }
}
function xs(e) {
  return (e = e.detail), typeof e == "object" && "data" in e ? e.data : null;
}
var Ht = !1;
function Od(e, t) {
  switch (e) {
    case "compositionend":
      return xs(t);
    case "keypress":
      return t.which !== 32 ? null : ((Oo = !0), zo);
    case "textInput":
      return (e = t.data), e === zo && Oo ? null : e;
    default:
      return null;
  }
}
function Id(e, t) {
  if (Ht)
    return e === "compositionend" || (!Ri && Es(e, t))
      ? ((e = Ss()), (Mr = Ci = it = null), (Ht = !1), e)
      : null;
  switch (e) {
    case "paste":
      return null;
    case "keypress":
      if (!(t.ctrlKey || t.altKey || t.metaKey) || (t.ctrlKey && t.altKey)) {
        if (t.char && 1 < t.char.length) return t.char;
        if (t.which) return String.fromCharCode(t.which);
      }
      return null;
    case "compositionend":
      return ks && t.locale !== "ko" ? null : t.data;
    default:
      return null;
  }
}
var Dd = {
  color: !0,
  date: !0,
  datetime: !0,
  "datetime-local": !0,
  email: !0,
  month: !0,
  number: !0,
  password: !0,
  range: !0,
  search: !0,
  tel: !0,
  text: !0,
  time: !0,
  url: !0,
  week: !0,
};
function Io(e) {
  var t = e && e.nodeName && e.nodeName.toLowerCase();
  return t === "input" ? !!Dd[e.type] : t === "textarea";
}
function Cs(e, t, n, r) {
  ts(r),
    (t = el(t, "onChange")),
    0 < t.length &&
      ((n = new Pi("onChange", "change", null, n, r)),
      e.push({ event: n, listeners: t }));
}
var Fn = null,
  Yn = null;
function Fd(e) {
  Fs(e, 0);
}
function kl(e) {
  var t = Qt(e);
  if (Xa(t)) return e;
}
function Md(e, t) {
  if (e === "change") return t;
}
var Ps = !1;
if (Ge) {
  var Xl;
  if (Ge) {
    var Gl = "oninput" in document;
    if (!Gl) {
      var Do = document.createElement("div");
      Do.setAttribute("oninput", "return;"),
        (Gl = typeof Do.oninput == "function");
    }
    Xl = Gl;
  } else Xl = !1;
  Ps = Xl && (!document.documentMode || 9 < document.documentMode);
}
function Fo() {
  Fn && (Fn.detachEvent("onpropertychange", _s), (Yn = Fn = null));
}
function _s(e) {
  if (e.propertyName === "value" && kl(Yn)) {
    var t = [];
    Cs(t, Yn, e, wi(e)), us(Fd, t);
  }
}
function $d(e, t, n) {
  e === "focusin"
    ? (Fo(), (Fn = t), (Yn = n), Fn.attachEvent("onpropertychange", _s))
    : e === "focusout" && Fo();
}
function jd(e) {
  if (e === "selectionchange" || e === "keyup" || e === "keydown")
    return kl(Yn);
}
function Ud(e, t) {
  if (e === "click") return kl(t);
}
function Ad(e, t) {
  if (e === "input" || e === "change") return kl(t);
}
function Bd(e, t) {
  return (e === t && (e !== 0 || 1 / e === 1 / t)) || (e !== e && t !== t);
}
var De = typeof Object.is == "function" ? Object.is : Bd;
function Xn(e, t) {
  if (De(e, t)) return !0;
  if (typeof e != "object" || e === null || typeof t != "object" || t === null)
    return !1;
  var n = Object.keys(e),
    r = Object.keys(t);
  if (n.length !== r.length) return !1;
  for (r = 0; r < n.length; r++) {
    var l = n[r];
    if (!hu.call(t, l) || !De(e[l], t[l])) return !1;
  }
  return !0;
}
function Mo(e) {
  for (; e && e.firstChild; ) e = e.firstChild;
  return e;
}
function $o(e, t) {
  var n = Mo(e);
  e = 0;
  for (var r; n; ) {
    if (n.nodeType === 3) {
      if (((r = e + n.textContent.length), e <= t && r >= t))
        return { node: n, offset: t - e };
      e = r;
    }
    e: {
      for (; n; ) {
        if (n.nextSibling) {
          n = n.nextSibling;
          break e;
        }
        n = n.parentNode;
      }
      n = void 0;
    }
    n = Mo(n);
  }
}
function Rs(e, t) {
  return e && t
    ? e === t
      ? !0
      : e && e.nodeType === 3
      ? !1
      : t && t.nodeType === 3
      ? Rs(e, t.parentNode)
      : "contains" in e
      ? e.contains(t)
      : e.compareDocumentPosition
      ? !!(e.compareDocumentPosition(t) & 16)
      : !1
    : !1;
}
function Ls() {
  for (var e = window, t = Yr(); t instanceof e.HTMLIFrameElement; ) {
    try {
      var n = typeof t.contentWindow.location.href == "string";
    } catch {
      n = !1;
    }
    if (n) e = t.contentWindow;
    else break;
    t = Yr(e.document);
  }
  return t;
}
function Li(e) {
  var t = e && e.nodeName && e.nodeName.toLowerCase();
  return (
    t &&
    ((t === "input" &&
      (e.type === "text" ||
        e.type === "search" ||
        e.type === "tel" ||
        e.type === "url" ||
        e.type === "password")) ||
      t === "textarea" ||
      e.contentEditable === "true")
  );
}
function Hd(e) {
  var t = Ls(),
    n = e.focusedElem,
    r = e.selectionRange;
  if (
    t !== n &&
    n &&
    n.ownerDocument &&
    Rs(n.ownerDocument.documentElement, n)
  ) {
    if (r !== null && Li(n)) {
      if (
        ((t = r.start),
        (e = r.end),
        e === void 0 && (e = t),
        "selectionStart" in n)
      )
        (n.selectionStart = t), (n.selectionEnd = Math.min(e, n.value.length));
      else if (
        ((e = ((t = n.ownerDocument || document) && t.defaultView) || window),
        e.getSelection)
      ) {
        e = e.getSelection();
        var l = n.textContent.length,
          u = Math.min(r.start, l);
        (r = r.end === void 0 ? u : Math.min(r.end, l)),
          !e.extend && u > r && ((l = r), (r = u), (u = l)),
          (l = $o(n, u));
        var i = $o(n, r);
        l &&
          i &&
          (e.rangeCount !== 1 ||
            e.anchorNode !== l.node ||
            e.anchorOffset !== l.offset ||
            e.focusNode !== i.node ||
            e.focusOffset !== i.offset) &&
          ((t = t.createRange()),
          t.setStart(l.node, l.offset),
          e.removeAllRanges(),
          u > r
            ? (e.addRange(t), e.extend(i.node, i.offset))
            : (t.setEnd(i.node, i.offset), e.addRange(t)));
      }
    }
    for (t = [], e = n; (e = e.parentNode); )
      e.nodeType === 1 &&
        t.push({ element: e, left: e.scrollLeft, top: e.scrollTop });
    for (typeof n.focus == "function" && n.focus(), n = 0; n < t.length; n++)
      (e = t[n]),
        (e.element.scrollLeft = e.left),
        (e.element.scrollTop = e.top);
  }
}
var Vd = Ge && "documentMode" in document && 11 >= document.documentMode,
  Vt = null,
  Iu = null,
  Mn = null,
  Du = !1;
function jo(e, t, n) {
  var r = n.window === n ? n.document : n.nodeType === 9 ? n : n.ownerDocument;
  Du ||
    Vt == null ||
    Vt !== Yr(r) ||
    ((r = Vt),
    "selectionStart" in r && Li(r)
      ? (r = { start: r.selectionStart, end: r.selectionEnd })
      : ((r = (
          (r.ownerDocument && r.ownerDocument.defaultView) ||
          window
        ).getSelection()),
        (r = {
          anchorNode: r.anchorNode,
          anchorOffset: r.anchorOffset,
          focusNode: r.focusNode,
          focusOffset: r.focusOffset,
        })),
    (Mn && Xn(Mn, r)) ||
      ((Mn = r),
      (r = el(Iu, "onSelect")),
      0 < r.length &&
        ((t = new Pi("onSelect", "select", null, t, n)),
        e.push({ event: t, listeners: r }),
        (t.target = Vt))));
}
function xr(e, t) {
  var n = {};
  return (
    (n[e.toLowerCase()] = t.toLowerCase()),
    (n["Webkit" + e] = "webkit" + t),
    (n["Moz" + e] = "moz" + t),
    n
  );
}
var Wt = {
    animationend: xr("Animation", "AnimationEnd"),
    animationiteration: xr("Animation", "AnimationIteration"),
    animationstart: xr("Animation", "AnimationStart"),
    transitionend: xr("Transition", "TransitionEnd"),
  },
  Zl = {},
  Ns = {};
Ge &&
  ((Ns = document.createElement("div").style),
  "AnimationEvent" in window ||
    (delete Wt.animationend.animation,
    delete Wt.animationiteration.animation,
    delete Wt.animationstart.animation),
  "TransitionEvent" in window || delete Wt.transitionend.transition);
function El(e) {
  if (Zl[e]) return Zl[e];
  if (!Wt[e]) return e;
  var t = Wt[e],
    n;
  for (n in t) if (t.hasOwnProperty(n) && n in Ns) return (Zl[e] = t[n]);
  return e;
}
var Ts = El("animationend"),
  zs = El("animationiteration"),
  Os = El("animationstart"),
  Is = El("transitionend"),
  Ds = new Map(),
  Uo =
    "abort auxClick cancel canPlay canPlayThrough click close contextMenu copy cut drag dragEnd dragEnter dragExit dragLeave dragOver dragStart drop durationChange emptied encrypted ended error gotPointerCapture input invalid keyDown keyPress keyUp load loadedData loadedMetadata loadStart lostPointerCapture mouseDown mouseMove mouseOut mouseOver mouseUp paste pause play playing pointerCancel pointerDown pointerMove pointerOut pointerOver pointerUp progress rateChange reset resize seeked seeking stalled submit suspend timeUpdate touchCancel touchEnd touchStart volumeChange scroll toggle touchMove waiting wheel".split(
      " "
    );
function wt(e, t) {
  Ds.set(e, t), $t(t, [e]);
}
for (var Jl = 0; Jl < Uo.length; Jl++) {
  var ql = Uo[Jl],
    Wd = ql.toLowerCase(),
    Qd = ql[0].toUpperCase() + ql.slice(1);
  wt(Wd, "on" + Qd);
}
wt(Ts, "onAnimationEnd");
wt(zs, "onAnimationIteration");
wt(Os, "onAnimationStart");
wt("dblclick", "onDoubleClick");
wt("focusin", "onFocus");
wt("focusout", "onBlur");
wt(Is, "onTransitionEnd");
ln("onMouseEnter", ["mouseout", "mouseover"]);
ln("onMouseLeave", ["mouseout", "mouseover"]);
ln("onPointerEnter", ["pointerout", "pointerover"]);
ln("onPointerLeave", ["pointerout", "pointerover"]);
$t(
  "onChange",
  "change click focusin focusout input keydown keyup selectionchange".split(" ")
);
$t(
  "onSelect",
  "focusout contextmenu dragend focusin keydown keyup mousedown mouseup selectionchange".split(
    " "
  )
);
$t("onBeforeInput", ["compositionend", "keypress", "textInput", "paste"]);
$t(
  "onCompositionEnd",
  "compositionend focusout keydown keypress keyup mousedown".split(" ")
);
$t(
  "onCompositionStart",
  "compositionstart focusout keydown keypress keyup mousedown".split(" ")
);
$t(
  "onCompositionUpdate",
  "compositionupdate focusout keydown keypress keyup mousedown".split(" ")
);
var zn =
    "abort canplay canplaythrough durationchange emptied encrypted ended error loadeddata loadedmetadata loadstart pause play playing progress ratechange resize seeked seeking stalled suspend timeupdate volumechange waiting".split(
      " "
    ),
  Kd = new Set("cancel close invalid load scroll toggle".split(" ").concat(zn));
function Ao(e, t, n) {
  var r = e.type || "unknown-event";
  (e.currentTarget = n), Wf(r, t, void 0, e), (e.currentTarget = null);
}
function Fs(e, t) {
  t = (t & 4) !== 0;
  for (var n = 0; n < e.length; n++) {
    var r = e[n],
      l = r.event;
    r = r.listeners;
    e: {
      var u = void 0;
      if (t)
        for (var i = r.length - 1; 0 <= i; i--) {
          var o = r[i],
            a = o.instance,
            s = o.currentTarget;
          if (((o = o.listener), a !== u && l.isPropagationStopped())) break e;
          Ao(l, o, s), (u = a);
        }
      else
        for (i = 0; i < r.length; i++) {
          if (
            ((o = r[i]),
            (a = o.instance),
            (s = o.currentTarget),
            (o = o.listener),
            a !== u && l.isPropagationStopped())
          )
            break e;
          Ao(l, o, s), (u = a);
        }
    }
  }
  if (Gr) throw ((e = Nu), (Gr = !1), (Nu = null), e);
}
function $(e, t) {
  var n = t[Uu];
  n === void 0 && (n = t[Uu] = new Set());
  var r = e + "__bubble";
  n.has(r) || (Ms(t, e, 2, !1), n.add(r));
}
function bl(e, t, n) {
  var r = 0;
  t && (r |= 4), Ms(n, e, r, t);
}
var Cr = "_reactListening" + Math.random().toString(36).slice(2);
function Gn(e) {
  if (!e[Cr]) {
    (e[Cr] = !0),
      Va.forEach(function (n) {
        n !== "selectionchange" && (Kd.has(n) || bl(n, !1, e), bl(n, !0, e));
      });
    var t = e.nodeType === 9 ? e : e.ownerDocument;
    t === null || t[Cr] || ((t[Cr] = !0), bl("selectionchange", !1, t));
  }
}
function Ms(e, t, n, r) {
  switch (ws(t)) {
    case 1:
      var l = id;
      break;
    case 4:
      l = od;
      break;
    default:
      l = xi;
  }
  (n = l.bind(null, t, n, e)),
    (l = void 0),
    !Lu ||
      (t !== "touchstart" && t !== "touchmove" && t !== "wheel") ||
      (l = !0),
    r
      ? l !== void 0
        ? e.addEventListener(t, n, { capture: !0, passive: l })
        : e.addEventListener(t, n, !0)
      : l !== void 0
      ? e.addEventListener(t, n, { passive: l })
      : e.addEventListener(t, n, !1);
}
function eu(e, t, n, r, l) {
  var u = r;
  if (!(t & 1) && !(t & 2) && r !== null)
    e: for (;;) {
      if (r === null) return;
      var i = r.tag;
      if (i === 3 || i === 4) {
        var o = r.stateNode.containerInfo;
        if (o === l || (o.nodeType === 8 && o.parentNode === l)) break;
        if (i === 4)
          for (i = r.return; i !== null; ) {
            var a = i.tag;
            if (
              (a === 3 || a === 4) &&
              ((a = i.stateNode.containerInfo),
              a === l || (a.nodeType === 8 && a.parentNode === l))
            )
              return;
            i = i.return;
          }
        for (; o !== null; ) {
          if (((i = Rt(o)), i === null)) return;
          if (((a = i.tag), a === 5 || a === 6)) {
            r = u = i;
            continue e;
          }
          o = o.parentNode;
        }
      }
      r = r.return;
    }
  us(function () {
    var s = u,
      h = wi(n),
      m = [];
    e: {
      var p = Ds.get(e);
      if (p !== void 0) {
        var y = Pi,
          S = e;
        switch (e) {
          case "keypress":
            if ($r(n) === 0) break e;
          case "keydown":
          case "keyup":
            y = Ed;
            break;
          case "focusin":
            (S = "focus"), (y = Yl);
            break;
          case "focusout":
            (S = "blur"), (y = Yl);
            break;
          case "beforeblur":
          case "afterblur":
            y = Yl;
            break;
          case "click":
            if (n.button === 2) break e;
          case "auxclick":
          case "dblclick":
          case "mousedown":
          case "mousemove":
          case "mouseup":
          case "mouseout":
          case "mouseover":
          case "contextmenu":
            y = Lo;
            break;
          case "drag":
          case "dragend":
          case "dragenter":
          case "dragexit":
          case "dragleave":
          case "dragover":
          case "dragstart":
          case "drop":
            y = cd;
            break;
          case "touchcancel":
          case "touchend":
          case "touchmove":
          case "touchstart":
            y = Pd;
            break;
          case Ts:
          case zs:
          case Os:
            y = pd;
            break;
          case Is:
            y = Rd;
            break;
          case "scroll":
            y = ad;
            break;
          case "wheel":
            y = Nd;
            break;
          case "copy":
          case "cut":
          case "paste":
            y = md;
            break;
          case "gotpointercapture":
          case "lostpointercapture":
          case "pointercancel":
          case "pointerdown":
          case "pointermove":
          case "pointerout":
          case "pointerover":
          case "pointerup":
            y = To;
        }
        var g = (t & 4) !== 0,
          x = !g && e === "scroll",
          f = g ? (p !== null ? p + "Capture" : null) : p;
        g = [];
        for (var c = s, d; c !== null; ) {
          d = c;
          var w = d.stateNode;
          if (
            (d.tag === 5 &&
              w !== null &&
              ((d = w),
              f !== null && ((w = Vn(c, f)), w != null && g.push(Zn(c, w, d)))),
            x)
          )
            break;
          c = c.return;
        }
        0 < g.length &&
          ((p = new y(p, S, null, n, h)), m.push({ event: p, listeners: g }));
      }
    }
    if (!(t & 7)) {
      e: {
        if (
          ((p = e === "mouseover" || e === "pointerover"),
          (y = e === "mouseout" || e === "pointerout"),
          p &&
            n !== _u &&
            (S = n.relatedTarget || n.fromElement) &&
            (Rt(S) || S[Ze]))
        )
          break e;
        if (
          (y || p) &&
          ((p =
            h.window === h
              ? h
              : (p = h.ownerDocument)
              ? p.defaultView || p.parentWindow
              : window),
          y
            ? ((S = n.relatedTarget || n.toElement),
              (y = s),
              (S = S ? Rt(S) : null),
              S !== null &&
                ((x = jt(S)), S !== x || (S.tag !== 5 && S.tag !== 6)) &&
                (S = null))
            : ((y = null), (S = s)),
          y !== S)
        ) {
          if (
            ((g = Lo),
            (w = "onMouseLeave"),
            (f = "onMouseEnter"),
            (c = "mouse"),
            (e === "pointerout" || e === "pointerover") &&
              ((g = To),
              (w = "onPointerLeave"),
              (f = "onPointerEnter"),
              (c = "pointer")),
            (x = y == null ? p : Qt(y)),
            (d = S == null ? p : Qt(S)),
            (p = new g(w, c + "leave", y, n, h)),
            (p.target = x),
            (p.relatedTarget = d),
            (w = null),
            Rt(h) === s &&
              ((g = new g(f, c + "enter", S, n, h)),
              (g.target = d),
              (g.relatedTarget = x),
              (w = g)),
            (x = w),
            y && S)
          )
            t: {
              for (g = y, f = S, c = 0, d = g; d; d = Ut(d)) c++;
              for (d = 0, w = f; w; w = Ut(w)) d++;
              for (; 0 < c - d; ) (g = Ut(g)), c--;
              for (; 0 < d - c; ) (f = Ut(f)), d--;
              for (; c--; ) {
                if (g === f || (f !== null && g === f.alternate)) break t;
                (g = Ut(g)), (f = Ut(f));
              }
              g = null;
            }
          else g = null;
          y !== null && Bo(m, p, y, g, !1),
            S !== null && x !== null && Bo(m, x, S, g, !0);
        }
      }
      e: {
        if (
          ((p = s ? Qt(s) : window),
          (y = p.nodeName && p.nodeName.toLowerCase()),
          y === "select" || (y === "input" && p.type === "file"))
        )
          var E = Md;
        else if (Io(p))
          if (Ps) E = Ad;
          else {
            E = jd;
            var R = $d;
          }
        else
          (y = p.nodeName) &&
            y.toLowerCase() === "input" &&
            (p.type === "checkbox" || p.type === "radio") &&
            (E = Ud);
        if (E && (E = E(e, s))) {
          Cs(m, E, n, h);
          break e;
        }
        R && R(e, p, s),
          e === "focusout" &&
            (R = p._wrapperState) &&
            R.controlled &&
            p.type === "number" &&
            ku(p, "number", p.value);
      }
      switch (((R = s ? Qt(s) : window), e)) {
        case "focusin":
          (Io(R) || R.contentEditable === "true") &&
            ((Vt = R), (Iu = s), (Mn = null));
          break;
        case "focusout":
          Mn = Iu = Vt = null;
          break;
        case "mousedown":
          Du = !0;
          break;
        case "contextmenu":
        case "mouseup":
        case "dragend":
          (Du = !1), jo(m, n, h);
          break;
        case "selectionchange":
          if (Vd) break;
        case "keydown":
        case "keyup":
          jo(m, n, h);
      }
      var _;
      if (Ri)
        e: {
          switch (e) {
            case "compositionstart":
              var L = "onCompositionStart";
              break e;
            case "compositionend":
              L = "onCompositionEnd";
              break e;
            case "compositionupdate":
              L = "onCompositionUpdate";
              break e;
          }
          L = void 0;
        }
      else
        Ht
          ? Es(e, n) && (L = "onCompositionEnd")
          : e === "keydown" && n.keyCode === 229 && (L = "onCompositionStart");
      L &&
        (ks &&
          n.locale !== "ko" &&
          (Ht || L !== "onCompositionStart"
            ? L === "onCompositionEnd" && Ht && (_ = Ss())
            : ((it = h),
              (Ci = "value" in it ? it.value : it.textContent),
              (Ht = !0))),
        (R = el(s, L)),
        0 < R.length &&
          ((L = new No(L, e, null, n, h)),
          m.push({ event: L, listeners: R }),
          _ ? (L.data = _) : ((_ = xs(n)), _ !== null && (L.data = _)))),
        (_ = zd ? Od(e, n) : Id(e, n)) &&
          ((s = el(s, "onBeforeInput")),
          0 < s.length &&
            ((h = new No("onBeforeInput", "beforeinput", null, n, h)),
            m.push({ event: h, listeners: s }),
            (h.data = _)));
    }
    Fs(m, t);
  });
}
function Zn(e, t, n) {
  return { instance: e, listener: t, currentTarget: n };
}
function el(e, t) {
  for (var n = t + "Capture", r = []; e !== null; ) {
    var l = e,
      u = l.stateNode;
    l.tag === 5 &&
      u !== null &&
      ((l = u),
      (u = Vn(e, n)),
      u != null && r.unshift(Zn(e, u, l)),
      (u = Vn(e, t)),
      u != null && r.push(Zn(e, u, l))),
      (e = e.return);
  }
  return r;
}
function Ut(e) {
  if (e === null) return null;
  do e = e.return;
  while (e && e.tag !== 5);
  return e || null;
}
function Bo(e, t, n, r, l) {
  for (var u = t._reactName, i = []; n !== null && n !== r; ) {
    var o = n,
      a = o.alternate,
      s = o.stateNode;
    if (a !== null && a === r) break;
    o.tag === 5 &&
      s !== null &&
      ((o = s),
      l
        ? ((a = Vn(n, u)), a != null && i.unshift(Zn(n, a, o)))
        : l || ((a = Vn(n, u)), a != null && i.push(Zn(n, a, o)))),
      (n = n.return);
  }
  i.length !== 0 && e.push({ event: t, listeners: i });
}
var Yd = /\r\n?/g,
  Xd = /\u0000|\uFFFD/g;
function Ho(e) {
  return (typeof e == "string" ? e : "" + e)
    .replace(
      Yd,
      `
`
    )
    .replace(Xd, "");
}
function Pr(e, t, n) {
  if (((t = Ho(t)), Ho(e) !== t && n)) throw Error(k(425));
}
function tl() {}
var Fu = null,
  Mu = null;
function $u(e, t) {
  return (
    e === "textarea" ||
    e === "noscript" ||
    typeof t.children == "string" ||
    typeof t.children == "number" ||
    (typeof t.dangerouslySetInnerHTML == "object" &&
      t.dangerouslySetInnerHTML !== null &&
      t.dangerouslySetInnerHTML.__html != null)
  );
}
var ju = typeof setTimeout == "function" ? setTimeout : void 0,
  Gd = typeof clearTimeout == "function" ? clearTimeout : void 0,
  Vo = typeof Promise == "function" ? Promise : void 0,
  Zd =
    typeof queueMicrotask == "function"
      ? queueMicrotask
      : typeof Vo < "u"
      ? function (e) {
          return Vo.resolve(null).then(e).catch(Jd);
        }
      : ju;
function Jd(e) {
  setTimeout(function () {
    throw e;
  });
}
function tu(e, t) {
  var n = t,
    r = 0;
  do {
    var l = n.nextSibling;
    if ((e.removeChild(n), l && l.nodeType === 8))
      if (((n = l.data), n === "/$")) {
        if (r === 0) {
          e.removeChild(l), Kn(t);
          return;
        }
        r--;
      } else (n !== "$" && n !== "$?" && n !== "$!") || r++;
    n = l;
  } while (n);
  Kn(t);
}
function ft(e) {
  for (; e != null; e = e.nextSibling) {
    var t = e.nodeType;
    if (t === 1 || t === 3) break;
    if (t === 8) {
      if (((t = e.data), t === "$" || t === "$!" || t === "$?")) break;
      if (t === "/$") return null;
    }
  }
  return e;
}
function Wo(e) {
  e = e.previousSibling;
  for (var t = 0; e; ) {
    if (e.nodeType === 8) {
      var n = e.data;
      if (n === "$" || n === "$!" || n === "$?") {
        if (t === 0) return e;
        t--;
      } else n === "/$" && t++;
    }
    e = e.previousSibling;
  }
  return null;
}
var hn = Math.random().toString(36).slice(2),
  je = "__reactFiber$" + hn,
  Jn = "__reactProps$" + hn,
  Ze = "__reactContainer$" + hn,
  Uu = "__reactEvents$" + hn,
  qd = "__reactListeners$" + hn,
  bd = "__reactHandles$" + hn;
function Rt(e) {
  var t = e[je];
  if (t) return t;
  for (var n = e.parentNode; n; ) {
    if ((t = n[Ze] || n[je])) {
      if (
        ((n = t.alternate),
        t.child !== null || (n !== null && n.child !== null))
      )
        for (e = Wo(e); e !== null; ) {
          if ((n = e[je])) return n;
          e = Wo(e);
        }
      return t;
    }
    (e = n), (n = e.parentNode);
  }
  return null;
}
function ar(e) {
  return (
    (e = e[je] || e[Ze]),
    !e || (e.tag !== 5 && e.tag !== 6 && e.tag !== 13 && e.tag !== 3) ? null : e
  );
}
function Qt(e) {
  if (e.tag === 5 || e.tag === 6) return e.stateNode;
  throw Error(k(33));
}
function xl(e) {
  return e[Jn] || null;
}
var Au = [],
  Kt = -1;
function St(e) {
  return { current: e };
}
function j(e) {
  0 > Kt || ((e.current = Au[Kt]), (Au[Kt] = null), Kt--);
}
function M(e, t) {
  Kt++, (Au[Kt] = e.current), (e.current = t);
}
var vt = {},
  ue = St(vt),
  pe = St(!1),
  Ot = vt;
function un(e, t) {
  var n = e.type.contextTypes;
  if (!n) return vt;
  var r = e.stateNode;
  if (r && r.__reactInternalMemoizedUnmaskedChildContext === t)
    return r.__reactInternalMemoizedMaskedChildContext;
  var l = {},
    u;
  for (u in n) l[u] = t[u];
  return (
    r &&
      ((e = e.stateNode),
      (e.__reactInternalMemoizedUnmaskedChildContext = t),
      (e.__reactInternalMemoizedMaskedChildContext = l)),
    l
  );
}
function he(e) {
  return (e = e.childContextTypes), e != null;
}
function nl() {
  j(pe), j(ue);
}
function Qo(e, t, n) {
  if (ue.current !== vt) throw Error(k(168));
  M(ue, t), M(pe, n);
}
function $s(e, t, n) {
  var r = e.stateNode;
  if (((t = t.childContextTypes), typeof r.getChildContext != "function"))
    return n;
  r = r.getChildContext();
  for (var l in r) if (!(l in t)) throw Error(k(108, $f(e) || "Unknown", l));
  return V({}, n, r);
}
function rl(e) {
  return (
    (e =
      ((e = e.stateNode) && e.__reactInternalMemoizedMergedChildContext) || vt),
    (Ot = ue.current),
    M(ue, e),
    M(pe, pe.current),
    !0
  );
}
function Ko(e, t, n) {
  var r = e.stateNode;
  if (!r) throw Error(k(169));
  n
    ? ((e = $s(e, t, Ot)),
      (r.__reactInternalMemoizedMergedChildContext = e),
      j(pe),
      j(ue),
      M(ue, e))
    : j(pe),
    M(pe, n);
}
var Ve = null,
  Cl = !1,
  nu = !1;
function js(e) {
  Ve === null ? (Ve = [e]) : Ve.push(e);
}
function ep(e) {
  (Cl = !0), js(e);
}
function kt() {
  if (!nu && Ve !== null) {
    nu = !0;
    var e = 0,
      t = F;
    try {
      var n = Ve;
      for (F = 1; e < n.length; e++) {
        var r = n[e];
        do r = r(!0);
        while (r !== null);
      }
      (Ve = null), (Cl = !1);
    } catch (l) {
      throw (Ve !== null && (Ve = Ve.slice(e + 1)), ss(Si, kt), l);
    } finally {
      (F = t), (nu = !1);
    }
  }
  return null;
}
var Yt = [],
  Xt = 0,
  ll = null,
  ul = 0,
  Ee = [],
  xe = 0,
  It = null,
  Qe = 1,
  Ke = "";
function Pt(e, t) {
  (Yt[Xt++] = ul), (Yt[Xt++] = ll), (ll = e), (ul = t);
}
function Us(e, t, n) {
  (Ee[xe++] = Qe), (Ee[xe++] = Ke), (Ee[xe++] = It), (It = e);
  var r = Qe;
  e = Ke;
  var l = 32 - Oe(r) - 1;
  (r &= ~(1 << l)), (n += 1);
  var u = 32 - Oe(t) + l;
  if (30 < u) {
    var i = l - (l % 5);
    (u = (r & ((1 << i) - 1)).toString(32)),
      (r >>= i),
      (l -= i),
      (Qe = (1 << (32 - Oe(t) + l)) | (n << l) | r),
      (Ke = u + e);
  } else (Qe = (1 << u) | (n << l) | r), (Ke = e);
}
function Ni(e) {
  e.return !== null && (Pt(e, 1), Us(e, 1, 0));
}
function Ti(e) {
  for (; e === ll; )
    (ll = Yt[--Xt]), (Yt[Xt] = null), (ul = Yt[--Xt]), (Yt[Xt] = null);
  for (; e === It; )
    (It = Ee[--xe]),
      (Ee[xe] = null),
      (Ke = Ee[--xe]),
      (Ee[xe] = null),
      (Qe = Ee[--xe]),
      (Ee[xe] = null);
}
var ge = null,
  ve = null,
  U = !1,
  ze = null;
function As(e, t) {
  var n = Ce(5, null, null, 0);
  (n.elementType = "DELETED"),
    (n.stateNode = t),
    (n.return = e),
    (t = e.deletions),
    t === null ? ((e.deletions = [n]), (e.flags |= 16)) : t.push(n);
}
function Yo(e, t) {
  switch (e.tag) {
    case 5:
      var n = e.type;
      return (
        (t =
          t.nodeType !== 1 || n.toLowerCase() !== t.nodeName.toLowerCase()
            ? null
            : t),
        t !== null
          ? ((e.stateNode = t), (ge = e), (ve = ft(t.firstChild)), !0)
          : !1
      );
    case 6:
      return (
        (t = e.pendingProps === "" || t.nodeType !== 3 ? null : t),
        t !== null ? ((e.stateNode = t), (ge = e), (ve = null), !0) : !1
      );
    case 13:
      return (
        (t = t.nodeType !== 8 ? null : t),
        t !== null
          ? ((n = It !== null ? { id: Qe, overflow: Ke } : null),
            (e.memoizedState = {
              dehydrated: t,
              treeContext: n,
              retryLane: 1073741824,
            }),
            (n = Ce(18, null, null, 0)),
            (n.stateNode = t),
            (n.return = e),
            (e.child = n),
            (ge = e),
            (ve = null),
            !0)
          : !1
      );
    default:
      return !1;
  }
}
function Bu(e) {
  return (e.mode & 1) !== 0 && (e.flags & 128) === 0;
}
function Hu(e) {
  if (U) {
    var t = ve;
    if (t) {
      var n = t;
      if (!Yo(e, t)) {
        if (Bu(e)) throw Error(k(418));
        t = ft(n.nextSibling);
        var r = ge;
        t && Yo(e, t)
          ? As(r, n)
          : ((e.flags = (e.flags & -4097) | 2), (U = !1), (ge = e));
      }
    } else {
      if (Bu(e)) throw Error(k(418));
      (e.flags = (e.flags & -4097) | 2), (U = !1), (ge = e);
    }
  }
}
function Xo(e) {
  for (e = e.return; e !== null && e.tag !== 5 && e.tag !== 3 && e.tag !== 13; )
    e = e.return;
  ge = e;
}
function _r(e) {
  if (e !== ge) return !1;
  if (!U) return Xo(e), (U = !0), !1;
  var t;
  if (
    ((t = e.tag !== 3) &&
      !(t = e.tag !== 5) &&
      ((t = e.type),
      (t = t !== "head" && t !== "body" && !$u(e.type, e.memoizedProps))),
    t && (t = ve))
  ) {
    if (Bu(e)) throw (Bs(), Error(k(418)));
    for (; t; ) As(e, t), (t = ft(t.nextSibling));
  }
  if ((Xo(e), e.tag === 13)) {
    if (((e = e.memoizedState), (e = e !== null ? e.dehydrated : null), !e))
      throw Error(k(317));
    e: {
      for (e = e.nextSibling, t = 0; e; ) {
        if (e.nodeType === 8) {
          var n = e.data;
          if (n === "/$") {
            if (t === 0) {
              ve = ft(e.nextSibling);
              break e;
            }
            t--;
          } else (n !== "$" && n !== "$!" && n !== "$?") || t++;
        }
        e = e.nextSibling;
      }
      ve = null;
    }
  } else ve = ge ? ft(e.stateNode.nextSibling) : null;
  return !0;
}
function Bs() {
  for (var e = ve; e; ) e = ft(e.nextSibling);
}
function on() {
  (ve = ge = null), (U = !1);
}
function zi(e) {
  ze === null ? (ze = [e]) : ze.push(e);
}
var tp = be.ReactCurrentBatchConfig;
function Cn(e, t, n) {
  if (
    ((e = n.ref), e !== null && typeof e != "function" && typeof e != "object")
  ) {
    if (n._owner) {
      if (((n = n._owner), n)) {
        if (n.tag !== 1) throw Error(k(309));
        var r = n.stateNode;
      }
      if (!r) throw Error(k(147, e));
      var l = r,
        u = "" + e;
      return t !== null &&
        t.ref !== null &&
        typeof t.ref == "function" &&
        t.ref._stringRef === u
        ? t.ref
        : ((t = function (i) {
            var o = l.refs;
            i === null ? delete o[u] : (o[u] = i);
          }),
          (t._stringRef = u),
          t);
    }
    if (typeof e != "string") throw Error(k(284));
    if (!n._owner) throw Error(k(290, e));
  }
  return e;
}
function Rr(e, t) {
  throw (
    ((e = Object.prototype.toString.call(t)),
    Error(
      k(
        31,
        e === "[object Object]"
          ? "object with keys {" + Object.keys(t).join(", ") + "}"
          : e
      )
    ))
  );
}
function Go(e) {
  var t = e._init;
  return t(e._payload);
}
function Hs(e) {
  function t(f, c) {
    if (e) {
      var d = f.deletions;
      d === null ? ((f.deletions = [c]), (f.flags |= 16)) : d.push(c);
    }
  }
  function n(f, c) {
    if (!e) return null;
    for (; c !== null; ) t(f, c), (c = c.sibling);
    return null;
  }
  function r(f, c) {
    for (f = new Map(); c !== null; )
      c.key !== null ? f.set(c.key, c) : f.set(c.index, c), (c = c.sibling);
    return f;
  }
  function l(f, c) {
    return (f = mt(f, c)), (f.index = 0), (f.sibling = null), f;
  }
  function u(f, c, d) {
    return (
      (f.index = d),
      e
        ? ((d = f.alternate),
          d !== null
            ? ((d = d.index), d < c ? ((f.flags |= 2), c) : d)
            : ((f.flags |= 2), c))
        : ((f.flags |= 1048576), c)
    );
  }
  function i(f) {
    return e && f.alternate === null && (f.flags |= 2), f;
  }
  function o(f, c, d, w) {
    return c === null || c.tag !== 6
      ? ((c = su(d, f.mode, w)), (c.return = f), c)
      : ((c = l(c, d)), (c.return = f), c);
  }
  function a(f, c, d, w) {
    var E = d.type;
    return E === Bt
      ? h(f, c, d.props.children, w, d.key)
      : c !== null &&
        (c.elementType === E ||
          (typeof E == "object" &&
            E !== null &&
            E.$$typeof === nt &&
            Go(E) === c.type))
      ? ((w = l(c, d.props)), (w.ref = Cn(f, c, d)), (w.return = f), w)
      : ((w = Wr(d.type, d.key, d.props, null, f.mode, w)),
        (w.ref = Cn(f, c, d)),
        (w.return = f),
        w);
  }
  function s(f, c, d, w) {
    return c === null ||
      c.tag !== 4 ||
      c.stateNode.containerInfo !== d.containerInfo ||
      c.stateNode.implementation !== d.implementation
      ? ((c = cu(d, f.mode, w)), (c.return = f), c)
      : ((c = l(c, d.children || [])), (c.return = f), c);
  }
  function h(f, c, d, w, E) {
    return c === null || c.tag !== 7
      ? ((c = zt(d, f.mode, w, E)), (c.return = f), c)
      : ((c = l(c, d)), (c.return = f), c);
  }
  function m(f, c, d) {
    if ((typeof c == "string" && c !== "") || typeof c == "number")
      return (c = su("" + c, f.mode, d)), (c.return = f), c;
    if (typeof c == "object" && c !== null) {
      switch (c.$$typeof) {
        case yr:
          return (
            (d = Wr(c.type, c.key, c.props, null, f.mode, d)),
            (d.ref = Cn(f, null, c)),
            (d.return = f),
            d
          );
        case At:
          return (c = cu(c, f.mode, d)), (c.return = f), c;
        case nt:
          var w = c._init;
          return m(f, w(c._payload), d);
      }
      if (Nn(c) || wn(c))
        return (c = zt(c, f.mode, d, null)), (c.return = f), c;
      Rr(f, c);
    }
    return null;
  }
  function p(f, c, d, w) {
    var E = c !== null ? c.key : null;
    if ((typeof d == "string" && d !== "") || typeof d == "number")
      return E !== null ? null : o(f, c, "" + d, w);
    if (typeof d == "object" && d !== null) {
      switch (d.$$typeof) {
        case yr:
          return d.key === E ? a(f, c, d, w) : null;
        case At:
          return d.key === E ? s(f, c, d, w) : null;
        case nt:
          return (E = d._init), p(f, c, E(d._payload), w);
      }
      if (Nn(d) || wn(d)) return E !== null ? null : h(f, c, d, w, null);
      Rr(f, d);
    }
    return null;
  }
  function y(f, c, d, w, E) {
    if ((typeof w == "string" && w !== "") || typeof w == "number")
      return (f = f.get(d) || null), o(c, f, "" + w, E);
    if (typeof w == "object" && w !== null) {
      switch (w.$$typeof) {
        case yr:
          return (f = f.get(w.key === null ? d : w.key) || null), a(c, f, w, E);
        case At:
          return (f = f.get(w.key === null ? d : w.key) || null), s(c, f, w, E);
        case nt:
          var R = w._init;
          return y(f, c, d, R(w._payload), E);
      }
      if (Nn(w) || wn(w)) return (f = f.get(d) || null), h(c, f, w, E, null);
      Rr(c, w);
    }
    return null;
  }
  function S(f, c, d, w) {
    for (
      var E = null, R = null, _ = c, L = (c = 0), I = null;
      _ !== null && L < d.length;
      L++
    ) {
      _.index > L ? ((I = _), (_ = null)) : (I = _.sibling);
      var N = p(f, _, d[L], w);
      if (N === null) {
        _ === null && (_ = I);
        break;
      }
      e && _ && N.alternate === null && t(f, _),
        (c = u(N, c, L)),
        R === null ? (E = N) : (R.sibling = N),
        (R = N),
        (_ = I);
    }
    if (L === d.length) return n(f, _), U && Pt(f, L), E;
    if (_ === null) {
      for (; L < d.length; L++)
        (_ = m(f, d[L], w)),
          _ !== null &&
            ((c = u(_, c, L)), R === null ? (E = _) : (R.sibling = _), (R = _));
      return U && Pt(f, L), E;
    }
    for (_ = r(f, _); L < d.length; L++)
      (I = y(_, f, L, d[L], w)),
        I !== null &&
          (e && I.alternate !== null && _.delete(I.key === null ? L : I.key),
          (c = u(I, c, L)),
          R === null ? (E = I) : (R.sibling = I),
          (R = I));
    return (
      e &&
        _.forEach(function (ce) {
          return t(f, ce);
        }),
      U && Pt(f, L),
      E
    );
  }
  function g(f, c, d, w) {
    var E = wn(d);
    if (typeof E != "function") throw Error(k(150));
    if (((d = E.call(d)), d == null)) throw Error(k(151));
    for (
      var R = (E = null), _ = c, L = (c = 0), I = null, N = d.next();
      _ !== null && !N.done;
      L++, N = d.next()
    ) {
      _.index > L ? ((I = _), (_ = null)) : (I = _.sibling);
      var ce = p(f, _, N.value, w);
      if (ce === null) {
        _ === null && (_ = I);
        break;
      }
      e && _ && ce.alternate === null && t(f, _),
        (c = u(ce, c, L)),
        R === null ? (E = ce) : (R.sibling = ce),
        (R = ce),
        (_ = I);
    }
    if (N.done) return n(f, _), U && Pt(f, L), E;
    if (_ === null) {
      for (; !N.done; L++, N = d.next())
        (N = m(f, N.value, w)),
          N !== null &&
            ((c = u(N, c, L)), R === null ? (E = N) : (R.sibling = N), (R = N));
      return U && Pt(f, L), E;
    }
    for (_ = r(f, _); !N.done; L++, N = d.next())
      (N = y(_, f, L, N.value, w)),
        N !== null &&
          (e && N.alternate !== null && _.delete(N.key === null ? L : N.key),
          (c = u(N, c, L)),
          R === null ? (E = N) : (R.sibling = N),
          (R = N));
    return (
      e &&
        _.forEach(function (vn) {
          return t(f, vn);
        }),
      U && Pt(f, L),
      E
    );
  }
  function x(f, c, d, w) {
    if (
      (typeof d == "object" &&
        d !== null &&
        d.type === Bt &&
        d.key === null &&
        (d = d.props.children),
      typeof d == "object" && d !== null)
    ) {
      switch (d.$$typeof) {
        case yr:
          e: {
            for (var E = d.key, R = c; R !== null; ) {
              if (R.key === E) {
                if (((E = d.type), E === Bt)) {
                  if (R.tag === 7) {
                    n(f, R.sibling),
                      (c = l(R, d.props.children)),
                      (c.return = f),
                      (f = c);
                    break e;
                  }
                } else if (
                  R.elementType === E ||
                  (typeof E == "object" &&
                    E !== null &&
                    E.$$typeof === nt &&
                    Go(E) === R.type)
                ) {
                  n(f, R.sibling),
                    (c = l(R, d.props)),
                    (c.ref = Cn(f, R, d)),
                    (c.return = f),
                    (f = c);
                  break e;
                }
                n(f, R);
                break;
              } else t(f, R);
              R = R.sibling;
            }
            d.type === Bt
              ? ((c = zt(d.props.children, f.mode, w, d.key)),
                (c.return = f),
                (f = c))
              : ((w = Wr(d.type, d.key, d.props, null, f.mode, w)),
                (w.ref = Cn(f, c, d)),
                (w.return = f),
                (f = w));
          }
          return i(f);
        case At:
          e: {
            for (R = d.key; c !== null; ) {
              if (c.key === R)
                if (
                  c.tag === 4 &&
                  c.stateNode.containerInfo === d.containerInfo &&
                  c.stateNode.implementation === d.implementation
                ) {
                  n(f, c.sibling),
                    (c = l(c, d.children || [])),
                    (c.return = f),
                    (f = c);
                  break e;
                } else {
                  n(f, c);
                  break;
                }
              else t(f, c);
              c = c.sibling;
            }
            (c = cu(d, f.mode, w)), (c.return = f), (f = c);
          }
          return i(f);
        case nt:
          return (R = d._init), x(f, c, R(d._payload), w);
      }
      if (Nn(d)) return S(f, c, d, w);
      if (wn(d)) return g(f, c, d, w);
      Rr(f, d);
    }
    return (typeof d == "string" && d !== "") || typeof d == "number"
      ? ((d = "" + d),
        c !== null && c.tag === 6
          ? (n(f, c.sibling), (c = l(c, d)), (c.return = f), (f = c))
          : (n(f, c), (c = su(d, f.mode, w)), (c.return = f), (f = c)),
        i(f))
      : n(f, c);
  }
  return x;
}
var an = Hs(!0),
  Vs = Hs(!1),
  il = St(null),
  ol = null,
  Gt = null,
  Oi = null;
function Ii() {
  Oi = Gt = ol = null;
}
function Di(e) {
  var t = il.current;
  j(il), (e._currentValue = t);
}
function Vu(e, t, n) {
  for (; e !== null; ) {
    var r = e.alternate;
    if (
      ((e.childLanes & t) !== t
        ? ((e.childLanes |= t), r !== null && (r.childLanes |= t))
        : r !== null && (r.childLanes & t) !== t && (r.childLanes |= t),
      e === n)
    )
      break;
    e = e.return;
  }
}
function nn(e, t) {
  (ol = e),
    (Oi = Gt = null),
    (e = e.dependencies),
    e !== null &&
      e.firstContext !== null &&
      (e.lanes & t && (de = !0), (e.firstContext = null));
}
function _e(e) {
  var t = e._currentValue;
  if (Oi !== e)
    if (((e = { context: e, memoizedValue: t, next: null }), Gt === null)) {
      if (ol === null) throw Error(k(308));
      (Gt = e), (ol.dependencies = { lanes: 0, firstContext: e });
    } else Gt = Gt.next = e;
  return t;
}
var Lt = null;
function Fi(e) {
  Lt === null ? (Lt = [e]) : Lt.push(e);
}
function Ws(e, t, n, r) {
  var l = t.interleaved;
  return (
    l === null ? ((n.next = n), Fi(t)) : ((n.next = l.next), (l.next = n)),
    (t.interleaved = n),
    Je(e, r)
  );
}
function Je(e, t) {
  e.lanes |= t;
  var n = e.alternate;
  for (n !== null && (n.lanes |= t), n = e, e = e.return; e !== null; )
    (e.childLanes |= t),
      (n = e.alternate),
      n !== null && (n.childLanes |= t),
      (n = e),
      (e = e.return);
  return n.tag === 3 ? n.stateNode : null;
}
var rt = !1;
function Mi(e) {
  e.updateQueue = {
    baseState: e.memoizedState,
    firstBaseUpdate: null,
    lastBaseUpdate: null,
    shared: { pending: null, interleaved: null, lanes: 0 },
    effects: null,
  };
}
function Qs(e, t) {
  (e = e.updateQueue),
    t.updateQueue === e &&
      (t.updateQueue = {
        baseState: e.baseState,
        firstBaseUpdate: e.firstBaseUpdate,
        lastBaseUpdate: e.lastBaseUpdate,
        shared: e.shared,
        effects: e.effects,
      });
}
function Ye(e, t) {
  return {
    eventTime: e,
    lane: t,
    tag: 0,
    payload: null,
    callback: null,
    next: null,
  };
}
function dt(e, t, n) {
  var r = e.updateQueue;
  if (r === null) return null;
  if (((r = r.shared), D & 2)) {
    var l = r.pending;
    return (
      l === null ? (t.next = t) : ((t.next = l.next), (l.next = t)),
      (r.pending = t),
      Je(e, n)
    );
  }
  return (
    (l = r.interleaved),
    l === null ? ((t.next = t), Fi(r)) : ((t.next = l.next), (l.next = t)),
    (r.interleaved = t),
    Je(e, n)
  );
}
function jr(e, t, n) {
  if (
    ((t = t.updateQueue), t !== null && ((t = t.shared), (n & 4194240) !== 0))
  ) {
    var r = t.lanes;
    (r &= e.pendingLanes), (n |= r), (t.lanes = n), ki(e, n);
  }
}
function Zo(e, t) {
  var n = e.updateQueue,
    r = e.alternate;
  if (r !== null && ((r = r.updateQueue), n === r)) {
    var l = null,
      u = null;
    if (((n = n.firstBaseUpdate), n !== null)) {
      do {
        var i = {
          eventTime: n.eventTime,
          lane: n.lane,
          tag: n.tag,
          payload: n.payload,
          callback: n.callback,
          next: null,
        };
        u === null ? (l = u = i) : (u = u.next = i), (n = n.next);
      } while (n !== null);
      u === null ? (l = u = t) : (u = u.next = t);
    } else l = u = t;
    (n = {
      baseState: r.baseState,
      firstBaseUpdate: l,
      lastBaseUpdate: u,
      shared: r.shared,
      effects: r.effects,
    }),
      (e.updateQueue = n);
    return;
  }
  (e = n.lastBaseUpdate),
    e === null ? (n.firstBaseUpdate = t) : (e.next = t),
    (n.lastBaseUpdate = t);
}
function al(e, t, n, r) {
  var l = e.updateQueue;
  rt = !1;
  var u = l.firstBaseUpdate,
    i = l.lastBaseUpdate,
    o = l.shared.pending;
  if (o !== null) {
    l.shared.pending = null;
    var a = o,
      s = a.next;
    (a.next = null), i === null ? (u = s) : (i.next = s), (i = a);
    var h = e.alternate;
    h !== null &&
      ((h = h.updateQueue),
      (o = h.lastBaseUpdate),
      o !== i &&
        (o === null ? (h.firstBaseUpdate = s) : (o.next = s),
        (h.lastBaseUpdate = a)));
  }
  if (u !== null) {
    var m = l.baseState;
    (i = 0), (h = s = a = null), (o = u);
    do {
      var p = o.lane,
        y = o.eventTime;
      if ((r & p) === p) {
        h !== null &&
          (h = h.next =
            {
              eventTime: y,
              lane: 0,
              tag: o.tag,
              payload: o.payload,
              callback: o.callback,
              next: null,
            });
        e: {
          var S = e,
            g = o;
          switch (((p = t), (y = n), g.tag)) {
            case 1:
              if (((S = g.payload), typeof S == "function")) {
                m = S.call(y, m, p);
                break e;
              }
              m = S;
              break e;
            case 3:
              S.flags = (S.flags & -65537) | 128;
            case 0:
              if (
                ((S = g.payload),
                (p = typeof S == "function" ? S.call(y, m, p) : S),
                p == null)
              )
                break e;
              m = V({}, m, p);
              break e;
            case 2:
              rt = !0;
          }
        }
        o.callback !== null &&
          o.lane !== 0 &&
          ((e.flags |= 64),
          (p = l.effects),
          p === null ? (l.effects = [o]) : p.push(o));
      } else
        (y = {
          eventTime: y,
          lane: p,
          tag: o.tag,
          payload: o.payload,
          callback: o.callback,
          next: null,
        }),
          h === null ? ((s = h = y), (a = m)) : (h = h.next = y),
          (i |= p);
      if (((o = o.next), o === null)) {
        if (((o = l.shared.pending), o === null)) break;
        (p = o),
          (o = p.next),
          (p.next = null),
          (l.lastBaseUpdate = p),
          (l.shared.pending = null);
      }
    } while (!0);
    if (
      (h === null && (a = m),
      (l.baseState = a),
      (l.firstBaseUpdate = s),
      (l.lastBaseUpdate = h),
      (t = l.shared.interleaved),
      t !== null)
    ) {
      l = t;
      do (i |= l.lane), (l = l.next);
      while (l !== t);
    } else u === null && (l.shared.lanes = 0);
    (Ft |= i), (e.lanes = i), (e.memoizedState = m);
  }
}
function Jo(e, t, n) {
  if (((e = t.effects), (t.effects = null), e !== null))
    for (t = 0; t < e.length; t++) {
      var r = e[t],
        l = r.callback;
      if (l !== null) {
        if (((r.callback = null), (r = n), typeof l != "function"))
          throw Error(k(191, l));
        l.call(r);
      }
    }
}
var sr = {},
  Ae = St(sr),
  qn = St(sr),
  bn = St(sr);
function Nt(e) {
  if (e === sr) throw Error(k(174));
  return e;
}
function $i(e, t) {
  switch ((M(bn, t), M(qn, e), M(Ae, sr), (e = t.nodeType), e)) {
    case 9:
    case 11:
      t = (t = t.documentElement) ? t.namespaceURI : xu(null, "");
      break;
    default:
      (e = e === 8 ? t.parentNode : t),
        (t = e.namespaceURI || null),
        (e = e.tagName),
        (t = xu(t, e));
  }
  j(Ae), M(Ae, t);
}
function sn() {
  j(Ae), j(qn), j(bn);
}
function Ks(e) {
  Nt(bn.current);
  var t = Nt(Ae.current),
    n = xu(t, e.type);
  t !== n && (M(qn, e), M(Ae, n));
}
function ji(e) {
  qn.current === e && (j(Ae), j(qn));
}
var A = St(0);
function sl(e) {
  for (var t = e; t !== null; ) {
    if (t.tag === 13) {
      var n = t.memoizedState;
      if (
        n !== null &&
        ((n = n.dehydrated), n === null || n.data === "$?" || n.data === "$!")
      )
        return t;
    } else if (t.tag === 19 && t.memoizedProps.revealOrder !== void 0) {
      if (t.flags & 128) return t;
    } else if (t.child !== null) {
      (t.child.return = t), (t = t.child);
      continue;
    }
    if (t === e) break;
    for (; t.sibling === null; ) {
      if (t.return === null || t.return === e) return null;
      t = t.return;
    }
    (t.sibling.return = t.return), (t = t.sibling);
  }
  return null;
}
var ru = [];
function Ui() {
  for (var e = 0; e < ru.length; e++)
    ru[e]._workInProgressVersionPrimary = null;
  ru.length = 0;
}
var Ur = be.ReactCurrentDispatcher,
  lu = be.ReactCurrentBatchConfig,
  Dt = 0,
  B = null,
  X = null,
  J = null,
  cl = !1,
  $n = !1,
  er = 0,
  np = 0;
function ne() {
  throw Error(k(321));
}
function Ai(e, t) {
  if (t === null) return !1;
  for (var n = 0; n < t.length && n < e.length; n++)
    if (!De(e[n], t[n])) return !1;
  return !0;
}
function Bi(e, t, n, r, l, u) {
  if (
    ((Dt = u),
    (B = t),
    (t.memoizedState = null),
    (t.updateQueue = null),
    (t.lanes = 0),
    (Ur.current = e === null || e.memoizedState === null ? ip : op),
    (e = n(r, l)),
    $n)
  ) {
    u = 0;
    do {
      if ((($n = !1), (er = 0), 25 <= u)) throw Error(k(301));
      (u += 1),
        (J = X = null),
        (t.updateQueue = null),
        (Ur.current = ap),
        (e = n(r, l));
    } while ($n);
  }
  if (
    ((Ur.current = fl),
    (t = X !== null && X.next !== null),
    (Dt = 0),
    (J = X = B = null),
    (cl = !1),
    t)
  )
    throw Error(k(300));
  return e;
}
function Hi() {
  var e = er !== 0;
  return (er = 0), e;
}
function $e() {
  var e = {
    memoizedState: null,
    baseState: null,
    baseQueue: null,
    queue: null,
    next: null,
  };
  return J === null ? (B.memoizedState = J = e) : (J = J.next = e), J;
}
function Re() {
  if (X === null) {
    var e = B.alternate;
    e = e !== null ? e.memoizedState : null;
  } else e = X.next;
  var t = J === null ? B.memoizedState : J.next;
  if (t !== null) (J = t), (X = e);
  else {
    if (e === null) throw Error(k(310));
    (X = e),
      (e = {
        memoizedState: X.memoizedState,
        baseState: X.baseState,
        baseQueue: X.baseQueue,
        queue: X.queue,
        next: null,
      }),
      J === null ? (B.memoizedState = J = e) : (J = J.next = e);
  }
  return J;
}
function tr(e, t) {
  return typeof t == "function" ? t(e) : t;
}
function uu(e) {
  var t = Re(),
    n = t.queue;
  if (n === null) throw Error(k(311));
  n.lastRenderedReducer = e;
  var r = X,
    l = r.baseQueue,
    u = n.pending;
  if (u !== null) {
    if (l !== null) {
      var i = l.next;
      (l.next = u.next), (u.next = i);
    }
    (r.baseQueue = l = u), (n.pending = null);
  }
  if (l !== null) {
    (u = l.next), (r = r.baseState);
    var o = (i = null),
      a = null,
      s = u;
    do {
      var h = s.lane;
      if ((Dt & h) === h)
        a !== null &&
          (a = a.next =
            {
              lane: 0,
              action: s.action,
              hasEagerState: s.hasEagerState,
              eagerState: s.eagerState,
              next: null,
            }),
          (r = s.hasEagerState ? s.eagerState : e(r, s.action));
      else {
        var m = {
          lane: h,
          action: s.action,
          hasEagerState: s.hasEagerState,
          eagerState: s.eagerState,
          next: null,
        };
        a === null ? ((o = a = m), (i = r)) : (a = a.next = m),
          (B.lanes |= h),
          (Ft |= h);
      }
      s = s.next;
    } while (s !== null && s !== u);
    a === null ? (i = r) : (a.next = o),
      De(r, t.memoizedState) || (de = !0),
      (t.memoizedState = r),
      (t.baseState = i),
      (t.baseQueue = a),
      (n.lastRenderedState = r);
  }
  if (((e = n.interleaved), e !== null)) {
    l = e;
    do (u = l.lane), (B.lanes |= u), (Ft |= u), (l = l.next);
    while (l !== e);
  } else l === null && (n.lanes = 0);
  return [t.memoizedState, n.dispatch];
}
function iu(e) {
  var t = Re(),
    n = t.queue;
  if (n === null) throw Error(k(311));
  n.lastRenderedReducer = e;
  var r = n.dispatch,
    l = n.pending,
    u = t.memoizedState;
  if (l !== null) {
    n.pending = null;
    var i = (l = l.next);
    do (u = e(u, i.action)), (i = i.next);
    while (i !== l);
    De(u, t.memoizedState) || (de = !0),
      (t.memoizedState = u),
      t.baseQueue === null && (t.baseState = u),
      (n.lastRenderedState = u);
  }
  return [u, r];
}
function Ys() {}
function Xs(e, t) {
  var n = B,
    r = Re(),
    l = t(),
    u = !De(r.memoizedState, l);
  if (
    (u && ((r.memoizedState = l), (de = !0)),
    (r = r.queue),
    Vi(Js.bind(null, n, r, e), [e]),
    r.getSnapshot !== t || u || (J !== null && J.memoizedState.tag & 1))
  ) {
    if (
      ((n.flags |= 2048),
      nr(9, Zs.bind(null, n, r, l, t), void 0, null),
      q === null)
    )
      throw Error(k(349));
    Dt & 30 || Gs(n, t, l);
  }
  return l;
}
function Gs(e, t, n) {
  (e.flags |= 16384),
    (e = { getSnapshot: t, value: n }),
    (t = B.updateQueue),
    t === null
      ? ((t = { lastEffect: null, stores: null }),
        (B.updateQueue = t),
        (t.stores = [e]))
      : ((n = t.stores), n === null ? (t.stores = [e]) : n.push(e));
}
function Zs(e, t, n, r) {
  (t.value = n), (t.getSnapshot = r), qs(t) && bs(e);
}
function Js(e, t, n) {
  return n(function () {
    qs(t) && bs(e);
  });
}
function qs(e) {
  var t = e.getSnapshot;
  e = e.value;
  try {
    var n = t();
    return !De(e, n);
  } catch {
    return !0;
  }
}
function bs(e) {
  var t = Je(e, 1);
  t !== null && Ie(t, e, 1, -1);
}
function qo(e) {
  var t = $e();
  return (
    typeof e == "function" && (e = e()),
    (t.memoizedState = t.baseState = e),
    (e = {
      pending: null,
      interleaved: null,
      lanes: 0,
      dispatch: null,
      lastRenderedReducer: tr,
      lastRenderedState: e,
    }),
    (t.queue = e),
    (e = e.dispatch = up.bind(null, B, e)),
    [t.memoizedState, e]
  );
}
function nr(e, t, n, r) {
  return (
    (e = { tag: e, create: t, destroy: n, deps: r, next: null }),
    (t = B.updateQueue),
    t === null
      ? ((t = { lastEffect: null, stores: null }),
        (B.updateQueue = t),
        (t.lastEffect = e.next = e))
      : ((n = t.lastEffect),
        n === null
          ? (t.lastEffect = e.next = e)
          : ((r = n.next), (n.next = e), (e.next = r), (t.lastEffect = e))),
    e
  );
}
function ec() {
  return Re().memoizedState;
}
function Ar(e, t, n, r) {
  var l = $e();
  (B.flags |= e),
    (l.memoizedState = nr(1 | t, n, void 0, r === void 0 ? null : r));
}
function Pl(e, t, n, r) {
  var l = Re();
  r = r === void 0 ? null : r;
  var u = void 0;
  if (X !== null) {
    var i = X.memoizedState;
    if (((u = i.destroy), r !== null && Ai(r, i.deps))) {
      l.memoizedState = nr(t, n, u, r);
      return;
    }
  }
  (B.flags |= e), (l.memoizedState = nr(1 | t, n, u, r));
}
function bo(e, t) {
  return Ar(8390656, 8, e, t);
}
function Vi(e, t) {
  return Pl(2048, 8, e, t);
}
function tc(e, t) {
  return Pl(4, 2, e, t);
}
function nc(e, t) {
  return Pl(4, 4, e, t);
}
function rc(e, t) {
  if (typeof t == "function")
    return (
      (e = e()),
      t(e),
      function () {
        t(null);
      }
    );
  if (t != null)
    return (
      (e = e()),
      (t.current = e),
      function () {
        t.current = null;
      }
    );
}
function lc(e, t, n) {
  return (
    (n = n != null ? n.concat([e]) : null), Pl(4, 4, rc.bind(null, t, e), n)
  );
}
function Wi() {}
function uc(e, t) {
  var n = Re();
  t = t === void 0 ? null : t;
  var r = n.memoizedState;
  return r !== null && t !== null && Ai(t, r[1])
    ? r[0]
    : ((n.memoizedState = [e, t]), e);
}
function ic(e, t) {
  var n = Re();
  t = t === void 0 ? null : t;
  var r = n.memoizedState;
  return r !== null && t !== null && Ai(t, r[1])
    ? r[0]
    : ((e = e()), (n.memoizedState = [e, t]), e);
}
function oc(e, t, n) {
  return Dt & 21
    ? (De(n, t) || ((n = ds()), (B.lanes |= n), (Ft |= n), (e.baseState = !0)),
      t)
    : (e.baseState && ((e.baseState = !1), (de = !0)), (e.memoizedState = n));
}
function rp(e, t) {
  var n = F;
  (F = n !== 0 && 4 > n ? n : 4), e(!0);
  var r = lu.transition;
  lu.transition = {};
  try {
    e(!1), t();
  } finally {
    (F = n), (lu.transition = r);
  }
}
function ac() {
  return Re().memoizedState;
}
function lp(e, t, n) {
  var r = ht(e);
  if (
    ((n = {
      lane: r,
      action: n,
      hasEagerState: !1,
      eagerState: null,
      next: null,
    }),
    sc(e))
  )
    cc(t, n);
  else if (((n = Ws(e, t, n, r)), n !== null)) {
    var l = oe();
    Ie(n, e, r, l), fc(n, t, r);
  }
}
function up(e, t, n) {
  var r = ht(e),
    l = { lane: r, action: n, hasEagerState: !1, eagerState: null, next: null };
  if (sc(e)) cc(t, l);
  else {
    var u = e.alternate;
    if (
      e.lanes === 0 &&
      (u === null || u.lanes === 0) &&
      ((u = t.lastRenderedReducer), u !== null)
    )
      try {
        var i = t.lastRenderedState,
          o = u(i, n);
        if (((l.hasEagerState = !0), (l.eagerState = o), De(o, i))) {
          var a = t.interleaved;
          a === null
            ? ((l.next = l), Fi(t))
            : ((l.next = a.next), (a.next = l)),
            (t.interleaved = l);
          return;
        }
      } catch {
      } finally {
      }
    (n = Ws(e, t, l, r)),
      n !== null && ((l = oe()), Ie(n, e, r, l), fc(n, t, r));
  }
}
function sc(e) {
  var t = e.alternate;
  return e === B || (t !== null && t === B);
}
function cc(e, t) {
  $n = cl = !0;
  var n = e.pending;
  n === null ? (t.next = t) : ((t.next = n.next), (n.next = t)),
    (e.pending = t);
}
function fc(e, t, n) {
  if (n & 4194240) {
    var r = t.lanes;
    (r &= e.pendingLanes), (n |= r), (t.lanes = n), ki(e, n);
  }
}
var fl = {
    readContext: _e,
    useCallback: ne,
    useContext: ne,
    useEffect: ne,
    useImperativeHandle: ne,
    useInsertionEffect: ne,
    useLayoutEffect: ne,
    useMemo: ne,
    useReducer: ne,
    useRef: ne,
    useState: ne,
    useDebugValue: ne,
    useDeferredValue: ne,
    useTransition: ne,
    useMutableSource: ne,
    useSyncExternalStore: ne,
    useId: ne,
    unstable_isNewReconciler: !1,
  },
  ip = {
    readContext: _e,
    useCallback: function (e, t) {
      return ($e().memoizedState = [e, t === void 0 ? null : t]), e;
    },
    useContext: _e,
    useEffect: bo,
    useImperativeHandle: function (e, t, n) {
      return (
        (n = n != null ? n.concat([e]) : null),
        Ar(4194308, 4, rc.bind(null, t, e), n)
      );
    },
    useLayoutEffect: function (e, t) {
      return Ar(4194308, 4, e, t);
    },
    useInsertionEffect: function (e, t) {
      return Ar(4, 2, e, t);
    },
    useMemo: function (e, t) {
      var n = $e();
      return (
        (t = t === void 0 ? null : t), (e = e()), (n.memoizedState = [e, t]), e
      );
    },
    useReducer: function (e, t, n) {
      var r = $e();
      return (
        (t = n !== void 0 ? n(t) : t),
        (r.memoizedState = r.baseState = t),
        (e = {
          pending: null,
          interleaved: null,
          lanes: 0,
          dispatch: null,
          lastRenderedReducer: e,
          lastRenderedState: t,
        }),
        (r.queue = e),
        (e = e.dispatch = lp.bind(null, B, e)),
        [r.memoizedState, e]
      );
    },
    useRef: function (e) {
      var t = $e();
      return (e = { current: e }), (t.memoizedState = e);
    },
    useState: qo,
    useDebugValue: Wi,
    useDeferredValue: function (e) {
      return ($e().memoizedState = e);
    },
    useTransition: function () {
      var e = qo(!1),
        t = e[0];
      return (e = rp.bind(null, e[1])), ($e().memoizedState = e), [t, e];
    },
    useMutableSource: function () {},
    useSyncExternalStore: function (e, t, n) {
      var r = B,
        l = $e();
      if (U) {
        if (n === void 0) throw Error(k(407));
        n = n();
      } else {
        if (((n = t()), q === null)) throw Error(k(349));
        Dt & 30 || Gs(r, t, n);
      }
      l.memoizedState = n;
      var u = { value: n, getSnapshot: t };
      return (
        (l.queue = u),
        bo(Js.bind(null, r, u, e), [e]),
        (r.flags |= 2048),
        nr(9, Zs.bind(null, r, u, n, t), void 0, null),
        n
      );
    },
    useId: function () {
      var e = $e(),
        t = q.identifierPrefix;
      if (U) {
        var n = Ke,
          r = Qe;
        (n = (r & ~(1 << (32 - Oe(r) - 1))).toString(32) + n),
          (t = ":" + t + "R" + n),
          (n = er++),
          0 < n && (t += "H" + n.toString(32)),
          (t += ":");
      } else (n = np++), (t = ":" + t + "r" + n.toString(32) + ":");
      return (e.memoizedState = t);
    },
    unstable_isNewReconciler: !1,
  },
  op = {
    readContext: _e,
    useCallback: uc,
    useContext: _e,
    useEffect: Vi,
    useImperativeHandle: lc,
    useInsertionEffect: tc,
    useLayoutEffect: nc,
    useMemo: ic,
    useReducer: uu,
    useRef: ec,
    useState: function () {
      return uu(tr);
    },
    useDebugValue: Wi,
    useDeferredValue: function (e) {
      var t = Re();
      return oc(t, X.memoizedState, e);
    },
    useTransition: function () {
      var e = uu(tr)[0],
        t = Re().memoizedState;
      return [e, t];
    },
    useMutableSource: Ys,
    useSyncExternalStore: Xs,
    useId: ac,
    unstable_isNewReconciler: !1,
  },
  ap = {
    readContext: _e,
    useCallback: uc,
    useContext: _e,
    useEffect: Vi,
    useImperativeHandle: lc,
    useInsertionEffect: tc,
    useLayoutEffect: nc,
    useMemo: ic,
    useReducer: iu,
    useRef: ec,
    useState: function () {
      return iu(tr);
    },
    useDebugValue: Wi,
    useDeferredValue: function (e) {
      var t = Re();
      return X === null ? (t.memoizedState = e) : oc(t, X.memoizedState, e);
    },
    useTransition: function () {
      var e = iu(tr)[0],
        t = Re().memoizedState;
      return [e, t];
    },
    useMutableSource: Ys,
    useSyncExternalStore: Xs,
    useId: ac,
    unstable_isNewReconciler: !1,
  };
function Ne(e, t) {
  if (e && e.defaultProps) {
    (t = V({}, t)), (e = e.defaultProps);
    for (var n in e) t[n] === void 0 && (t[n] = e[n]);
    return t;
  }
  return t;
}
function Wu(e, t, n, r) {
  (t = e.memoizedState),
    (n = n(r, t)),
    (n = n == null ? t : V({}, t, n)),
    (e.memoizedState = n),
    e.lanes === 0 && (e.updateQueue.baseState = n);
}
var _l = {
  isMounted: function (e) {
    return (e = e._reactInternals) ? jt(e) === e : !1;
  },
  enqueueSetState: function (e, t, n) {
    e = e._reactInternals;
    var r = oe(),
      l = ht(e),
      u = Ye(r, l);
    (u.payload = t),
      n != null && (u.callback = n),
      (t = dt(e, u, l)),
      t !== null && (Ie(t, e, l, r), jr(t, e, l));
  },
  enqueueReplaceState: function (e, t, n) {
    e = e._reactInternals;
    var r = oe(),
      l = ht(e),
      u = Ye(r, l);
    (u.tag = 1),
      (u.payload = t),
      n != null && (u.callback = n),
      (t = dt(e, u, l)),
      t !== null && (Ie(t, e, l, r), jr(t, e, l));
  },
  enqueueForceUpdate: function (e, t) {
    e = e._reactInternals;
    var n = oe(),
      r = ht(e),
      l = Ye(n, r);
    (l.tag = 2),
      t != null && (l.callback = t),
      (t = dt(e, l, r)),
      t !== null && (Ie(t, e, r, n), jr(t, e, r));
  },
};
function ea(e, t, n, r, l, u, i) {
  return (
    (e = e.stateNode),
    typeof e.shouldComponentUpdate == "function"
      ? e.shouldComponentUpdate(r, u, i)
      : t.prototype && t.prototype.isPureReactComponent
      ? !Xn(n, r) || !Xn(l, u)
      : !0
  );
}
function dc(e, t, n) {
  var r = !1,
    l = vt,
    u = t.contextType;
  return (
    typeof u == "object" && u !== null
      ? (u = _e(u))
      : ((l = he(t) ? Ot : ue.current),
        (r = t.contextTypes),
        (u = (r = r != null) ? un(e, l) : vt)),
    (t = new t(n, u)),
    (e.memoizedState = t.state !== null && t.state !== void 0 ? t.state : null),
    (t.updater = _l),
    (e.stateNode = t),
    (t._reactInternals = e),
    r &&
      ((e = e.stateNode),
      (e.__reactInternalMemoizedUnmaskedChildContext = l),
      (e.__reactInternalMemoizedMaskedChildContext = u)),
    t
  );
}
function ta(e, t, n, r) {
  (e = t.state),
    typeof t.componentWillReceiveProps == "function" &&
      t.componentWillReceiveProps(n, r),
    typeof t.UNSAFE_componentWillReceiveProps == "function" &&
      t.UNSAFE_componentWillReceiveProps(n, r),
    t.state !== e && _l.enqueueReplaceState(t, t.state, null);
}
function Qu(e, t, n, r) {
  var l = e.stateNode;
  (l.props = n), (l.state = e.memoizedState), (l.refs = {}), Mi(e);
  var u = t.contextType;
  typeof u == "object" && u !== null
    ? (l.context = _e(u))
    : ((u = he(t) ? Ot : ue.current), (l.context = un(e, u))),
    (l.state = e.memoizedState),
    (u = t.getDerivedStateFromProps),
    typeof u == "function" && (Wu(e, t, u, n), (l.state = e.memoizedState)),
    typeof t.getDerivedStateFromProps == "function" ||
      typeof l.getSnapshotBeforeUpdate == "function" ||
      (typeof l.UNSAFE_componentWillMount != "function" &&
        typeof l.componentWillMount != "function") ||
      ((t = l.state),
      typeof l.componentWillMount == "function" && l.componentWillMount(),
      typeof l.UNSAFE_componentWillMount == "function" &&
        l.UNSAFE_componentWillMount(),
      t !== l.state && _l.enqueueReplaceState(l, l.state, null),
      al(e, n, l, r),
      (l.state = e.memoizedState)),
    typeof l.componentDidMount == "function" && (e.flags |= 4194308);
}
function cn(e, t) {
  try {
    var n = "",
      r = t;
    do (n += Mf(r)), (r = r.return);
    while (r);
    var l = n;
  } catch (u) {
    l =
      `
Error generating stack: ` +
      u.message +
      `
` +
      u.stack;
  }
  return { value: e, source: t, stack: l, digest: null };
}
function ou(e, t, n) {
  return { value: e, source: null, stack: n ?? null, digest: t ?? null };
}
function Ku(e, t) {
  try {
    console.error(t.value);
  } catch (n) {
    setTimeout(function () {
      throw n;
    });
  }
}
var sp = typeof WeakMap == "function" ? WeakMap : Map;
function pc(e, t, n) {
  (n = Ye(-1, n)), (n.tag = 3), (n.payload = { element: null });
  var r = t.value;
  return (
    (n.callback = function () {
      pl || ((pl = !0), (ni = r)), Ku(e, t);
    }),
    n
  );
}
function hc(e, t, n) {
  (n = Ye(-1, n)), (n.tag = 3);
  var r = e.type.getDerivedStateFromError;
  if (typeof r == "function") {
    var l = t.value;
    (n.payload = function () {
      return r(l);
    }),
      (n.callback = function () {
        Ku(e, t);
      });
  }
  var u = e.stateNode;
  return (
    u !== null &&
      typeof u.componentDidCatch == "function" &&
      (n.callback = function () {
        Ku(e, t),
          typeof r != "function" &&
            (pt === null ? (pt = new Set([this])) : pt.add(this));
        var i = t.stack;
        this.componentDidCatch(t.value, {
          componentStack: i !== null ? i : "",
        });
      }),
    n
  );
}
function na(e, t, n) {
  var r = e.pingCache;
  if (r === null) {
    r = e.pingCache = new sp();
    var l = new Set();
    r.set(t, l);
  } else (l = r.get(t)), l === void 0 && ((l = new Set()), r.set(t, l));
  l.has(n) || (l.add(n), (e = xp.bind(null, e, t, n)), t.then(e, e));
}
function ra(e) {
  do {
    var t;
    if (
      ((t = e.tag === 13) &&
        ((t = e.memoizedState), (t = t !== null ? t.dehydrated !== null : !0)),
      t)
    )
      return e;
    e = e.return;
  } while (e !== null);
  return null;
}
function la(e, t, n, r, l) {
  return e.mode & 1
    ? ((e.flags |= 65536), (e.lanes = l), e)
    : (e === t
        ? (e.flags |= 65536)
        : ((e.flags |= 128),
          (n.flags |= 131072),
          (n.flags &= -52805),
          n.tag === 1 &&
            (n.alternate === null
              ? (n.tag = 17)
              : ((t = Ye(-1, 1)), (t.tag = 2), dt(n, t, 1))),
          (n.lanes |= 1)),
      e);
}
var cp = be.ReactCurrentOwner,
  de = !1;
function ie(e, t, n, r) {
  t.child = e === null ? Vs(t, null, n, r) : an(t, e.child, n, r);
}
function ua(e, t, n, r, l) {
  n = n.render;
  var u = t.ref;
  return (
    nn(t, l),
    (r = Bi(e, t, n, r, u, l)),
    (n = Hi()),
    e !== null && !de
      ? ((t.updateQueue = e.updateQueue),
        (t.flags &= -2053),
        (e.lanes &= ~l),
        qe(e, t, l))
      : (U && n && Ni(t), (t.flags |= 1), ie(e, t, r, l), t.child)
  );
}
function ia(e, t, n, r, l) {
  if (e === null) {
    var u = n.type;
    return typeof u == "function" &&
      !qi(u) &&
      u.defaultProps === void 0 &&
      n.compare === null &&
      n.defaultProps === void 0
      ? ((t.tag = 15), (t.type = u), mc(e, t, u, r, l))
      : ((e = Wr(n.type, null, r, t, t.mode, l)),
        (e.ref = t.ref),
        (e.return = t),
        (t.child = e));
  }
  if (((u = e.child), !(e.lanes & l))) {
    var i = u.memoizedProps;
    if (
      ((n = n.compare), (n = n !== null ? n : Xn), n(i, r) && e.ref === t.ref)
    )
      return qe(e, t, l);
  }
  return (
    (t.flags |= 1),
    (e = mt(u, r)),
    (e.ref = t.ref),
    (e.return = t),
    (t.child = e)
  );
}
function mc(e, t, n, r, l) {
  if (e !== null) {
    var u = e.memoizedProps;
    if (Xn(u, r) && e.ref === t.ref)
      if (((de = !1), (t.pendingProps = r = u), (e.lanes & l) !== 0))
        e.flags & 131072 && (de = !0);
      else return (t.lanes = e.lanes), qe(e, t, l);
  }
  return Yu(e, t, n, r, l);
}
function yc(e, t, n) {
  var r = t.pendingProps,
    l = r.children,
    u = e !== null ? e.memoizedState : null;
  if (r.mode === "hidden")
    if (!(t.mode & 1))
      (t.memoizedState = { baseLanes: 0, cachePool: null, transitions: null }),
        M(Jt, ye),
        (ye |= n);
    else {
      if (!(n & 1073741824))
        return (
          (e = u !== null ? u.baseLanes | n : n),
          (t.lanes = t.childLanes = 1073741824),
          (t.memoizedState = {
            baseLanes: e,
            cachePool: null,
            transitions: null,
          }),
          (t.updateQueue = null),
          M(Jt, ye),
          (ye |= e),
          null
        );
      (t.memoizedState = { baseLanes: 0, cachePool: null, transitions: null }),
        (r = u !== null ? u.baseLanes : n),
        M(Jt, ye),
        (ye |= r);
    }
  else
    u !== null ? ((r = u.baseLanes | n), (t.memoizedState = null)) : (r = n),
      M(Jt, ye),
      (ye |= r);
  return ie(e, t, l, n), t.child;
}
function vc(e, t) {
  var n = t.ref;
  ((e === null && n !== null) || (e !== null && e.ref !== n)) &&
    ((t.flags |= 512), (t.flags |= 2097152));
}
function Yu(e, t, n, r, l) {
  var u = he(n) ? Ot : ue.current;
  return (
    (u = un(t, u)),
    nn(t, l),
    (n = Bi(e, t, n, r, u, l)),
    (r = Hi()),
    e !== null && !de
      ? ((t.updateQueue = e.updateQueue),
        (t.flags &= -2053),
        (e.lanes &= ~l),
        qe(e, t, l))
      : (U && r && Ni(t), (t.flags |= 1), ie(e, t, n, l), t.child)
  );
}
function oa(e, t, n, r, l) {
  if (he(n)) {
    var u = !0;
    rl(t);
  } else u = !1;
  if ((nn(t, l), t.stateNode === null))
    Br(e, t), dc(t, n, r), Qu(t, n, r, l), (r = !0);
  else if (e === null) {
    var i = t.stateNode,
      o = t.memoizedProps;
    i.props = o;
    var a = i.context,
      s = n.contextType;
    typeof s == "object" && s !== null
      ? (s = _e(s))
      : ((s = he(n) ? Ot : ue.current), (s = un(t, s)));
    var h = n.getDerivedStateFromProps,
      m =
        typeof h == "function" ||
        typeof i.getSnapshotBeforeUpdate == "function";
    m ||
      (typeof i.UNSAFE_componentWillReceiveProps != "function" &&
        typeof i.componentWillReceiveProps != "function") ||
      ((o !== r || a !== s) && ta(t, i, r, s)),
      (rt = !1);
    var p = t.memoizedState;
    (i.state = p),
      al(t, r, i, l),
      (a = t.memoizedState),
      o !== r || p !== a || pe.current || rt
        ? (typeof h == "function" && (Wu(t, n, h, r), (a = t.memoizedState)),
          (o = rt || ea(t, n, o, r, p, a, s))
            ? (m ||
                (typeof i.UNSAFE_componentWillMount != "function" &&
                  typeof i.componentWillMount != "function") ||
                (typeof i.componentWillMount == "function" &&
                  i.componentWillMount(),
                typeof i.UNSAFE_componentWillMount == "function" &&
                  i.UNSAFE_componentWillMount()),
              typeof i.componentDidMount == "function" && (t.flags |= 4194308))
            : (typeof i.componentDidMount == "function" && (t.flags |= 4194308),
              (t.memoizedProps = r),
              (t.memoizedState = a)),
          (i.props = r),
          (i.state = a),
          (i.context = s),
          (r = o))
        : (typeof i.componentDidMount == "function" && (t.flags |= 4194308),
          (r = !1));
  } else {
    (i = t.stateNode),
      Qs(e, t),
      (o = t.memoizedProps),
      (s = t.type === t.elementType ? o : Ne(t.type, o)),
      (i.props = s),
      (m = t.pendingProps),
      (p = i.context),
      (a = n.contextType),
      typeof a == "object" && a !== null
        ? (a = _e(a))
        : ((a = he(n) ? Ot : ue.current), (a = un(t, a)));
    var y = n.getDerivedStateFromProps;
    (h =
      typeof y == "function" ||
      typeof i.getSnapshotBeforeUpdate == "function") ||
      (typeof i.UNSAFE_componentWillReceiveProps != "function" &&
        typeof i.componentWillReceiveProps != "function") ||
      ((o !== m || p !== a) && ta(t, i, r, a)),
      (rt = !1),
      (p = t.memoizedState),
      (i.state = p),
      al(t, r, i, l);
    var S = t.memoizedState;
    o !== m || p !== S || pe.current || rt
      ? (typeof y == "function" && (Wu(t, n, y, r), (S = t.memoizedState)),
        (s = rt || ea(t, n, s, r, p, S, a) || !1)
          ? (h ||
              (typeof i.UNSAFE_componentWillUpdate != "function" &&
                typeof i.componentWillUpdate != "function") ||
              (typeof i.componentWillUpdate == "function" &&
                i.componentWillUpdate(r, S, a),
              typeof i.UNSAFE_componentWillUpdate == "function" &&
                i.UNSAFE_componentWillUpdate(r, S, a)),
            typeof i.componentDidUpdate == "function" && (t.flags |= 4),
            typeof i.getSnapshotBeforeUpdate == "function" && (t.flags |= 1024))
          : (typeof i.componentDidUpdate != "function" ||
              (o === e.memoizedProps && p === e.memoizedState) ||
              (t.flags |= 4),
            typeof i.getSnapshotBeforeUpdate != "function" ||
              (o === e.memoizedProps && p === e.memoizedState) ||
              (t.flags |= 1024),
            (t.memoizedProps = r),
            (t.memoizedState = S)),
        (i.props = r),
        (i.state = S),
        (i.context = a),
        (r = s))
      : (typeof i.componentDidUpdate != "function" ||
          (o === e.memoizedProps && p === e.memoizedState) ||
          (t.flags |= 4),
        typeof i.getSnapshotBeforeUpdate != "function" ||
          (o === e.memoizedProps && p === e.memoizedState) ||
          (t.flags |= 1024),
        (r = !1));
  }
  return Xu(e, t, n, r, u, l);
}
function Xu(e, t, n, r, l, u) {
  vc(e, t);
  var i = (t.flags & 128) !== 0;
  if (!r && !i) return l && Ko(t, n, !1), qe(e, t, u);
  (r = t.stateNode), (cp.current = t);
  var o =
    i && typeof n.getDerivedStateFromError != "function" ? null : r.render();
  return (
    (t.flags |= 1),
    e !== null && i
      ? ((t.child = an(t, e.child, null, u)), (t.child = an(t, null, o, u)))
      : ie(e, t, o, u),
    (t.memoizedState = r.state),
    l && Ko(t, n, !0),
    t.child
  );
}
function gc(e) {
  var t = e.stateNode;
  t.pendingContext
    ? Qo(e, t.pendingContext, t.pendingContext !== t.context)
    : t.context && Qo(e, t.context, !1),
    $i(e, t.containerInfo);
}
function aa(e, t, n, r, l) {
  return on(), zi(l), (t.flags |= 256), ie(e, t, n, r), t.child;
}
var Gu = { dehydrated: null, treeContext: null, retryLane: 0 };
function Zu(e) {
  return { baseLanes: e, cachePool: null, transitions: null };
}
function wc(e, t, n) {
  var r = t.pendingProps,
    l = A.current,
    u = !1,
    i = (t.flags & 128) !== 0,
    o;
  if (
    ((o = i) ||
      (o = e !== null && e.memoizedState === null ? !1 : (l & 2) !== 0),
    o
      ? ((u = !0), (t.flags &= -129))
      : (e === null || e.memoizedState !== null) && (l |= 1),
    M(A, l & 1),
    e === null)
  )
    return (
      Hu(t),
      (e = t.memoizedState),
      e !== null && ((e = e.dehydrated), e !== null)
        ? (t.mode & 1
            ? e.data === "$!"
              ? (t.lanes = 8)
              : (t.lanes = 1073741824)
            : (t.lanes = 1),
          null)
        : ((i = r.children),
          (e = r.fallback),
          u
            ? ((r = t.mode),
              (u = t.child),
              (i = { mode: "hidden", children: i }),
              !(r & 1) && u !== null
                ? ((u.childLanes = 0), (u.pendingProps = i))
                : (u = Nl(i, r, 0, null)),
              (e = zt(e, r, n, null)),
              (u.return = t),
              (e.return = t),
              (u.sibling = e),
              (t.child = u),
              (t.child.memoizedState = Zu(n)),
              (t.memoizedState = Gu),
              e)
            : Qi(t, i))
    );
  if (((l = e.memoizedState), l !== null && ((o = l.dehydrated), o !== null)))
    return fp(e, t, i, r, o, l, n);
  if (u) {
    (u = r.fallback), (i = t.mode), (l = e.child), (o = l.sibling);
    var a = { mode: "hidden", children: r.children };
    return (
      !(i & 1) && t.child !== l
        ? ((r = t.child),
          (r.childLanes = 0),
          (r.pendingProps = a),
          (t.deletions = null))
        : ((r = mt(l, a)), (r.subtreeFlags = l.subtreeFlags & 14680064)),
      o !== null ? (u = mt(o, u)) : ((u = zt(u, i, n, null)), (u.flags |= 2)),
      (u.return = t),
      (r.return = t),
      (r.sibling = u),
      (t.child = r),
      (r = u),
      (u = t.child),
      (i = e.child.memoizedState),
      (i =
        i === null
          ? Zu(n)
          : {
              baseLanes: i.baseLanes | n,
              cachePool: null,
              transitions: i.transitions,
            }),
      (u.memoizedState = i),
      (u.childLanes = e.childLanes & ~n),
      (t.memoizedState = Gu),
      r
    );
  }
  return (
    (u = e.child),
    (e = u.sibling),
    (r = mt(u, { mode: "visible", children: r.children })),
    !(t.mode & 1) && (r.lanes = n),
    (r.return = t),
    (r.sibling = null),
    e !== null &&
      ((n = t.deletions),
      n === null ? ((t.deletions = [e]), (t.flags |= 16)) : n.push(e)),
    (t.child = r),
    (t.memoizedState = null),
    r
  );
}
function Qi(e, t) {
  return (
    (t = Nl({ mode: "visible", children: t }, e.mode, 0, null)),
    (t.return = e),
    (e.child = t)
  );
}
function Lr(e, t, n, r) {
  return (
    r !== null && zi(r),
    an(t, e.child, null, n),
    (e = Qi(t, t.pendingProps.children)),
    (e.flags |= 2),
    (t.memoizedState = null),
    e
  );
}
function fp(e, t, n, r, l, u, i) {
  if (n)
    return t.flags & 256
      ? ((t.flags &= -257), (r = ou(Error(k(422)))), Lr(e, t, i, r))
      : t.memoizedState !== null
      ? ((t.child = e.child), (t.flags |= 128), null)
      : ((u = r.fallback),
        (l = t.mode),
        (r = Nl({ mode: "visible", children: r.children }, l, 0, null)),
        (u = zt(u, l, i, null)),
        (u.flags |= 2),
        (r.return = t),
        (u.return = t),
        (r.sibling = u),
        (t.child = r),
        t.mode & 1 && an(t, e.child, null, i),
        (t.child.memoizedState = Zu(i)),
        (t.memoizedState = Gu),
        u);
  if (!(t.mode & 1)) return Lr(e, t, i, null);
  if (l.data === "$!") {
    if (((r = l.nextSibling && l.nextSibling.dataset), r)) var o = r.dgst;
    return (r = o), (u = Error(k(419))), (r = ou(u, r, void 0)), Lr(e, t, i, r);
  }
  if (((o = (i & e.childLanes) !== 0), de || o)) {
    if (((r = q), r !== null)) {
      switch (i & -i) {
        case 4:
          l = 2;
          break;
        case 16:
          l = 8;
          break;
        case 64:
        case 128:
        case 256:
        case 512:
        case 1024:
        case 2048:
        case 4096:
        case 8192:
        case 16384:
        case 32768:
        case 65536:
        case 131072:
        case 262144:
        case 524288:
        case 1048576:
        case 2097152:
        case 4194304:
        case 8388608:
        case 16777216:
        case 33554432:
        case 67108864:
          l = 32;
          break;
        case 536870912:
          l = 268435456;
          break;
        default:
          l = 0;
      }
      (l = l & (r.suspendedLanes | i) ? 0 : l),
        l !== 0 &&
          l !== u.retryLane &&
          ((u.retryLane = l), Je(e, l), Ie(r, e, l, -1));
    }
    return Ji(), (r = ou(Error(k(421)))), Lr(e, t, i, r);
  }
  return l.data === "$?"
    ? ((t.flags |= 128),
      (t.child = e.child),
      (t = Cp.bind(null, e)),
      (l._reactRetry = t),
      null)
    : ((e = u.treeContext),
      (ve = ft(l.nextSibling)),
      (ge = t),
      (U = !0),
      (ze = null),
      e !== null &&
        ((Ee[xe++] = Qe),
        (Ee[xe++] = Ke),
        (Ee[xe++] = It),
        (Qe = e.id),
        (Ke = e.overflow),
        (It = t)),
      (t = Qi(t, r.children)),
      (t.flags |= 4096),
      t);
}
function sa(e, t, n) {
  e.lanes |= t;
  var r = e.alternate;
  r !== null && (r.lanes |= t), Vu(e.return, t, n);
}
function au(e, t, n, r, l) {
  var u = e.memoizedState;
  u === null
    ? (e.memoizedState = {
        isBackwards: t,
        rendering: null,
        renderingStartTime: 0,
        last: r,
        tail: n,
        tailMode: l,
      })
    : ((u.isBackwards = t),
      (u.rendering = null),
      (u.renderingStartTime = 0),
      (u.last = r),
      (u.tail = n),
      (u.tailMode = l));
}
function Sc(e, t, n) {
  var r = t.pendingProps,
    l = r.revealOrder,
    u = r.tail;
  if ((ie(e, t, r.children, n), (r = A.current), r & 2))
    (r = (r & 1) | 2), (t.flags |= 128);
  else {
    if (e !== null && e.flags & 128)
      e: for (e = t.child; e !== null; ) {
        if (e.tag === 13) e.memoizedState !== null && sa(e, n, t);
        else if (e.tag === 19) sa(e, n, t);
        else if (e.child !== null) {
          (e.child.return = e), (e = e.child);
          continue;
        }
        if (e === t) break e;
        for (; e.sibling === null; ) {
          if (e.return === null || e.return === t) break e;
          e = e.return;
        }
        (e.sibling.return = e.return), (e = e.sibling);
      }
    r &= 1;
  }
  if ((M(A, r), !(t.mode & 1))) t.memoizedState = null;
  else
    switch (l) {
      case "forwards":
        for (n = t.child, l = null; n !== null; )
          (e = n.alternate),
            e !== null && sl(e) === null && (l = n),
            (n = n.sibling);
        (n = l),
          n === null
            ? ((l = t.child), (t.child = null))
            : ((l = n.sibling), (n.sibling = null)),
          au(t, !1, l, n, u);
        break;
      case "backwards":
        for (n = null, l = t.child, t.child = null; l !== null; ) {
          if (((e = l.alternate), e !== null && sl(e) === null)) {
            t.child = l;
            break;
          }
          (e = l.sibling), (l.sibling = n), (n = l), (l = e);
        }
        au(t, !0, n, null, u);
        break;
      case "together":
        au(t, !1, null, null, void 0);
        break;
      default:
        t.memoizedState = null;
    }
  return t.child;
}
function Br(e, t) {
  !(t.mode & 1) &&
    e !== null &&
    ((e.alternate = null), (t.alternate = null), (t.flags |= 2));
}
function qe(e, t, n) {
  if (
    (e !== null && (t.dependencies = e.dependencies),
    (Ft |= t.lanes),
    !(n & t.childLanes))
  )
    return null;
  if (e !== null && t.child !== e.child) throw Error(k(153));
  if (t.child !== null) {
    for (
      e = t.child, n = mt(e, e.pendingProps), t.child = n, n.return = t;
      e.sibling !== null;

    )
      (e = e.sibling), (n = n.sibling = mt(e, e.pendingProps)), (n.return = t);
    n.sibling = null;
  }
  return t.child;
}
function dp(e, t, n) {
  switch (t.tag) {
    case 3:
      gc(t), on();
      break;
    case 5:
      Ks(t);
      break;
    case 1:
      he(t.type) && rl(t);
      break;
    case 4:
      $i(t, t.stateNode.containerInfo);
      break;
    case 10:
      var r = t.type._context,
        l = t.memoizedProps.value;
      M(il, r._currentValue), (r._currentValue = l);
      break;
    case 13:
      if (((r = t.memoizedState), r !== null))
        return r.dehydrated !== null
          ? (M(A, A.current & 1), (t.flags |= 128), null)
          : n & t.child.childLanes
          ? wc(e, t, n)
          : (M(A, A.current & 1),
            (e = qe(e, t, n)),
            e !== null ? e.sibling : null);
      M(A, A.current & 1);
      break;
    case 19:
      if (((r = (n & t.childLanes) !== 0), e.flags & 128)) {
        if (r) return Sc(e, t, n);
        t.flags |= 128;
      }
      if (
        ((l = t.memoizedState),
        l !== null &&
          ((l.rendering = null), (l.tail = null), (l.lastEffect = null)),
        M(A, A.current),
        r)
      )
        break;
      return null;
    case 22:
    case 23:
      return (t.lanes = 0), yc(e, t, n);
  }
  return qe(e, t, n);
}
var kc, Ju, Ec, xc;
kc = function (e, t) {
  for (var n = t.child; n !== null; ) {
    if (n.tag === 5 || n.tag === 6) e.appendChild(n.stateNode);
    else if (n.tag !== 4 && n.child !== null) {
      (n.child.return = n), (n = n.child);
      continue;
    }
    if (n === t) break;
    for (; n.sibling === null; ) {
      if (n.return === null || n.return === t) return;
      n = n.return;
    }
    (n.sibling.return = n.return), (n = n.sibling);
  }
};
Ju = function () {};
Ec = function (e, t, n, r) {
  var l = e.memoizedProps;
  if (l !== r) {
    (e = t.stateNode), Nt(Ae.current);
    var u = null;
    switch (n) {
      case "input":
        (l = wu(e, l)), (r = wu(e, r)), (u = []);
        break;
      case "select":
        (l = V({}, l, { value: void 0 })),
          (r = V({}, r, { value: void 0 })),
          (u = []);
        break;
      case "textarea":
        (l = Eu(e, l)), (r = Eu(e, r)), (u = []);
        break;
      default:
        typeof l.onClick != "function" &&
          typeof r.onClick == "function" &&
          (e.onclick = tl);
    }
    Cu(n, r);
    var i;
    n = null;
    for (s in l)
      if (!r.hasOwnProperty(s) && l.hasOwnProperty(s) && l[s] != null)
        if (s === "style") {
          var o = l[s];
          for (i in o) o.hasOwnProperty(i) && (n || (n = {}), (n[i] = ""));
        } else
          s !== "dangerouslySetInnerHTML" &&
            s !== "children" &&
            s !== "suppressContentEditableWarning" &&
            s !== "suppressHydrationWarning" &&
            s !== "autoFocus" &&
            (Bn.hasOwnProperty(s)
              ? u || (u = [])
              : (u = u || []).push(s, null));
    for (s in r) {
      var a = r[s];
      if (
        ((o = l != null ? l[s] : void 0),
        r.hasOwnProperty(s) && a !== o && (a != null || o != null))
      )
        if (s === "style")
          if (o) {
            for (i in o)
              !o.hasOwnProperty(i) ||
                (a && a.hasOwnProperty(i)) ||
                (n || (n = {}), (n[i] = ""));
            for (i in a)
              a.hasOwnProperty(i) &&
                o[i] !== a[i] &&
                (n || (n = {}), (n[i] = a[i]));
          } else n || (u || (u = []), u.push(s, n)), (n = a);
        else
          s === "dangerouslySetInnerHTML"
            ? ((a = a ? a.__html : void 0),
              (o = o ? o.__html : void 0),
              a != null && o !== a && (u = u || []).push(s, a))
            : s === "children"
            ? (typeof a != "string" && typeof a != "number") ||
              (u = u || []).push(s, "" + a)
            : s !== "suppressContentEditableWarning" &&
              s !== "suppressHydrationWarning" &&
              (Bn.hasOwnProperty(s)
                ? (a != null && s === "onScroll" && $("scroll", e),
                  u || o === a || (u = []))
                : (u = u || []).push(s, a));
    }
    n && (u = u || []).push("style", n);
    var s = u;
    (t.updateQueue = s) && (t.flags |= 4);
  }
};
xc = function (e, t, n, r) {
  n !== r && (t.flags |= 4);
};
function Pn(e, t) {
  if (!U)
    switch (e.tailMode) {
      case "hidden":
        t = e.tail;
        for (var n = null; t !== null; )
          t.alternate !== null && (n = t), (t = t.sibling);
        n === null ? (e.tail = null) : (n.sibling = null);
        break;
      case "collapsed":
        n = e.tail;
        for (var r = null; n !== null; )
          n.alternate !== null && (r = n), (n = n.sibling);
        r === null
          ? t || e.tail === null
            ? (e.tail = null)
            : (e.tail.sibling = null)
          : (r.sibling = null);
    }
}
function re(e) {
  var t = e.alternate !== null && e.alternate.child === e.child,
    n = 0,
    r = 0;
  if (t)
    for (var l = e.child; l !== null; )
      (n |= l.lanes | l.childLanes),
        (r |= l.subtreeFlags & 14680064),
        (r |= l.flags & 14680064),
        (l.return = e),
        (l = l.sibling);
  else
    for (l = e.child; l !== null; )
      (n |= l.lanes | l.childLanes),
        (r |= l.subtreeFlags),
        (r |= l.flags),
        (l.return = e),
        (l = l.sibling);
  return (e.subtreeFlags |= r), (e.childLanes = n), t;
}
function pp(e, t, n) {
  var r = t.pendingProps;
  switch ((Ti(t), t.tag)) {
    case 2:
    case 16:
    case 15:
    case 0:
    case 11:
    case 7:
    case 8:
    case 12:
    case 9:
    case 14:
      return re(t), null;
    case 1:
      return he(t.type) && nl(), re(t), null;
    case 3:
      return (
        (r = t.stateNode),
        sn(),
        j(pe),
        j(ue),
        Ui(),
        r.pendingContext &&
          ((r.context = r.pendingContext), (r.pendingContext = null)),
        (e === null || e.child === null) &&
          (_r(t)
            ? (t.flags |= 4)
            : e === null ||
              (e.memoizedState.isDehydrated && !(t.flags & 256)) ||
              ((t.flags |= 1024), ze !== null && (ui(ze), (ze = null)))),
        Ju(e, t),
        re(t),
        null
      );
    case 5:
      ji(t);
      var l = Nt(bn.current);
      if (((n = t.type), e !== null && t.stateNode != null))
        Ec(e, t, n, r, l),
          e.ref !== t.ref && ((t.flags |= 512), (t.flags |= 2097152));
      else {
        if (!r) {
          if (t.stateNode === null) throw Error(k(166));
          return re(t), null;
        }
        if (((e = Nt(Ae.current)), _r(t))) {
          (r = t.stateNode), (n = t.type);
          var u = t.memoizedProps;
          switch (((r[je] = t), (r[Jn] = u), (e = (t.mode & 1) !== 0), n)) {
            case "dialog":
              $("cancel", r), $("close", r);
              break;
            case "iframe":
            case "object":
            case "embed":
              $("load", r);
              break;
            case "video":
            case "audio":
              for (l = 0; l < zn.length; l++) $(zn[l], r);
              break;
            case "source":
              $("error", r);
              break;
            case "img":
            case "image":
            case "link":
              $("error", r), $("load", r);
              break;
            case "details":
              $("toggle", r);
              break;
            case "input":
              go(r, u), $("invalid", r);
              break;
            case "select":
              (r._wrapperState = { wasMultiple: !!u.multiple }),
                $("invalid", r);
              break;
            case "textarea":
              So(r, u), $("invalid", r);
          }
          Cu(n, u), (l = null);
          for (var i in u)
            if (u.hasOwnProperty(i)) {
              var o = u[i];
              i === "children"
                ? typeof o == "string"
                  ? r.textContent !== o &&
                    (u.suppressHydrationWarning !== !0 &&
                      Pr(r.textContent, o, e),
                    (l = ["children", o]))
                  : typeof o == "number" &&
                    r.textContent !== "" + o &&
                    (u.suppressHydrationWarning !== !0 &&
                      Pr(r.textContent, o, e),
                    (l = ["children", "" + o]))
                : Bn.hasOwnProperty(i) &&
                  o != null &&
                  i === "onScroll" &&
                  $("scroll", r);
            }
          switch (n) {
            case "input":
              vr(r), wo(r, u, !0);
              break;
            case "textarea":
              vr(r), ko(r);
              break;
            case "select":
            case "option":
              break;
            default:
              typeof u.onClick == "function" && (r.onclick = tl);
          }
          (r = l), (t.updateQueue = r), r !== null && (t.flags |= 4);
        } else {
          (i = l.nodeType === 9 ? l : l.ownerDocument),
            e === "http://www.w3.org/1999/xhtml" && (e = Ja(n)),
            e === "http://www.w3.org/1999/xhtml"
              ? n === "script"
                ? ((e = i.createElement("div")),
                  (e.innerHTML = "<script></script>"),
                  (e = e.removeChild(e.firstChild)))
                : typeof r.is == "string"
                ? (e = i.createElement(n, { is: r.is }))
                : ((e = i.createElement(n)),
                  n === "select" &&
                    ((i = e),
                    r.multiple
                      ? (i.multiple = !0)
                      : r.size && (i.size = r.size)))
              : (e = i.createElementNS(e, n)),
            (e[je] = t),
            (e[Jn] = r),
            kc(e, t, !1, !1),
            (t.stateNode = e);
          e: {
            switch (((i = Pu(n, r)), n)) {
              case "dialog":
                $("cancel", e), $("close", e), (l = r);
                break;
              case "iframe":
              case "object":
              case "embed":
                $("load", e), (l = r);
                break;
              case "video":
              case "audio":
                for (l = 0; l < zn.length; l++) $(zn[l], e);
                l = r;
                break;
              case "source":
                $("error", e), (l = r);
                break;
              case "img":
              case "image":
              case "link":
                $("error", e), $("load", e), (l = r);
                break;
              case "details":
                $("toggle", e), (l = r);
                break;
              case "input":
                go(e, r), (l = wu(e, r)), $("invalid", e);
                break;
              case "option":
                l = r;
                break;
              case "select":
                (e._wrapperState = { wasMultiple: !!r.multiple }),
                  (l = V({}, r, { value: void 0 })),
                  $("invalid", e);
                break;
              case "textarea":
                So(e, r), (l = Eu(e, r)), $("invalid", e);
                break;
              default:
                l = r;
            }
            Cu(n, l), (o = l);
            for (u in o)
              if (o.hasOwnProperty(u)) {
                var a = o[u];
                u === "style"
                  ? es(e, a)
                  : u === "dangerouslySetInnerHTML"
                  ? ((a = a ? a.__html : void 0), a != null && qa(e, a))
                  : u === "children"
                  ? typeof a == "string"
                    ? (n !== "textarea" || a !== "") && Hn(e, a)
                    : typeof a == "number" && Hn(e, "" + a)
                  : u !== "suppressContentEditableWarning" &&
                    u !== "suppressHydrationWarning" &&
                    u !== "autoFocus" &&
                    (Bn.hasOwnProperty(u)
                      ? a != null && u === "onScroll" && $("scroll", e)
                      : a != null && mi(e, u, a, i));
              }
            switch (n) {
              case "input":
                vr(e), wo(e, r, !1);
                break;
              case "textarea":
                vr(e), ko(e);
                break;
              case "option":
                r.value != null && e.setAttribute("value", "" + yt(r.value));
                break;
              case "select":
                (e.multiple = !!r.multiple),
                  (u = r.value),
                  u != null
                    ? qt(e, !!r.multiple, u, !1)
                    : r.defaultValue != null &&
                      qt(e, !!r.multiple, r.defaultValue, !0);
                break;
              default:
                typeof l.onClick == "function" && (e.onclick = tl);
            }
            switch (n) {
              case "button":
              case "input":
              case "select":
              case "textarea":
                r = !!r.autoFocus;
                break e;
              case "img":
                r = !0;
                break e;
              default:
                r = !1;
            }
          }
          r && (t.flags |= 4);
        }
        t.ref !== null && ((t.flags |= 512), (t.flags |= 2097152));
      }
      return re(t), null;
    case 6:
      if (e && t.stateNode != null) xc(e, t, e.memoizedProps, r);
      else {
        if (typeof r != "string" && t.stateNode === null) throw Error(k(166));
        if (((n = Nt(bn.current)), Nt(Ae.current), _r(t))) {
          if (
            ((r = t.stateNode),
            (n = t.memoizedProps),
            (r[je] = t),
            (u = r.nodeValue !== n) && ((e = ge), e !== null))
          )
            switch (e.tag) {
              case 3:
                Pr(r.nodeValue, n, (e.mode & 1) !== 0);
                break;
              case 5:
                e.memoizedProps.suppressHydrationWarning !== !0 &&
                  Pr(r.nodeValue, n, (e.mode & 1) !== 0);
            }
          u && (t.flags |= 4);
        } else
          (r = (n.nodeType === 9 ? n : n.ownerDocument).createTextNode(r)),
            (r[je] = t),
            (t.stateNode = r);
      }
      return re(t), null;
    case 13:
      if (
        (j(A),
        (r = t.memoizedState),
        e === null ||
          (e.memoizedState !== null && e.memoizedState.dehydrated !== null))
      ) {
        if (U && ve !== null && t.mode & 1 && !(t.flags & 128))
          Bs(), on(), (t.flags |= 98560), (u = !1);
        else if (((u = _r(t)), r !== null && r.dehydrated !== null)) {
          if (e === null) {
            if (!u) throw Error(k(318));
            if (
              ((u = t.memoizedState),
              (u = u !== null ? u.dehydrated : null),
              !u)
            )
              throw Error(k(317));
            u[je] = t;
          } else
            on(), !(t.flags & 128) && (t.memoizedState = null), (t.flags |= 4);
          re(t), (u = !1);
        } else ze !== null && (ui(ze), (ze = null)), (u = !0);
        if (!u) return t.flags & 65536 ? t : null;
      }
      return t.flags & 128
        ? ((t.lanes = n), t)
        : ((r = r !== null),
          r !== (e !== null && e.memoizedState !== null) &&
            r &&
            ((t.child.flags |= 8192),
            t.mode & 1 &&
              (e === null || A.current & 1 ? G === 0 && (G = 3) : Ji())),
          t.updateQueue !== null && (t.flags |= 4),
          re(t),
          null);
    case 4:
      return (
        sn(), Ju(e, t), e === null && Gn(t.stateNode.containerInfo), re(t), null
      );
    case 10:
      return Di(t.type._context), re(t), null;
    case 17:
      return he(t.type) && nl(), re(t), null;
    case 19:
      if ((j(A), (u = t.memoizedState), u === null)) return re(t), null;
      if (((r = (t.flags & 128) !== 0), (i = u.rendering), i === null))
        if (r) Pn(u, !1);
        else {
          if (G !== 0 || (e !== null && e.flags & 128))
            for (e = t.child; e !== null; ) {
              if (((i = sl(e)), i !== null)) {
                for (
                  t.flags |= 128,
                    Pn(u, !1),
                    r = i.updateQueue,
                    r !== null && ((t.updateQueue = r), (t.flags |= 4)),
                    t.subtreeFlags = 0,
                    r = n,
                    n = t.child;
                  n !== null;

                )
                  (u = n),
                    (e = r),
                    (u.flags &= 14680066),
                    (i = u.alternate),
                    i === null
                      ? ((u.childLanes = 0),
                        (u.lanes = e),
                        (u.child = null),
                        (u.subtreeFlags = 0),
                        (u.memoizedProps = null),
                        (u.memoizedState = null),
                        (u.updateQueue = null),
                        (u.dependencies = null),
                        (u.stateNode = null))
                      : ((u.childLanes = i.childLanes),
                        (u.lanes = i.lanes),
                        (u.child = i.child),
                        (u.subtreeFlags = 0),
                        (u.deletions = null),
                        (u.memoizedProps = i.memoizedProps),
                        (u.memoizedState = i.memoizedState),
                        (u.updateQueue = i.updateQueue),
                        (u.type = i.type),
                        (e = i.dependencies),
                        (u.dependencies =
                          e === null
                            ? null
                            : {
                                lanes: e.lanes,
                                firstContext: e.firstContext,
                              })),
                    (n = n.sibling);
                return M(A, (A.current & 1) | 2), t.child;
              }
              e = e.sibling;
            }
          u.tail !== null &&
            K() > fn &&
            ((t.flags |= 128), (r = !0), Pn(u, !1), (t.lanes = 4194304));
        }
      else {
        if (!r)
          if (((e = sl(i)), e !== null)) {
            if (
              ((t.flags |= 128),
              (r = !0),
              (n = e.updateQueue),
              n !== null && ((t.updateQueue = n), (t.flags |= 4)),
              Pn(u, !0),
              u.tail === null && u.tailMode === "hidden" && !i.alternate && !U)
            )
              return re(t), null;
          } else
            2 * K() - u.renderingStartTime > fn &&
              n !== 1073741824 &&
              ((t.flags |= 128), (r = !0), Pn(u, !1), (t.lanes = 4194304));
        u.isBackwards
          ? ((i.sibling = t.child), (t.child = i))
          : ((n = u.last),
            n !== null ? (n.sibling = i) : (t.child = i),
            (u.last = i));
      }
      return u.tail !== null
        ? ((t = u.tail),
          (u.rendering = t),
          (u.tail = t.sibling),
          (u.renderingStartTime = K()),
          (t.sibling = null),
          (n = A.current),
          M(A, r ? (n & 1) | 2 : n & 1),
          t)
        : (re(t), null);
    case 22:
    case 23:
      return (
        Zi(),
        (r = t.memoizedState !== null),
        e !== null && (e.memoizedState !== null) !== r && (t.flags |= 8192),
        r && t.mode & 1
          ? ye & 1073741824 && (re(t), t.subtreeFlags & 6 && (t.flags |= 8192))
          : re(t),
        null
      );
    case 24:
      return null;
    case 25:
      return null;
  }
  throw Error(k(156, t.tag));
}
function hp(e, t) {
  switch ((Ti(t), t.tag)) {
    case 1:
      return (
        he(t.type) && nl(),
        (e = t.flags),
        e & 65536 ? ((t.flags = (e & -65537) | 128), t) : null
      );
    case 3:
      return (
        sn(),
        j(pe),
        j(ue),
        Ui(),
        (e = t.flags),
        e & 65536 && !(e & 128) ? ((t.flags = (e & -65537) | 128), t) : null
      );
    case 5:
      return ji(t), null;
    case 13:
      if ((j(A), (e = t.memoizedState), e !== null && e.dehydrated !== null)) {
        if (t.alternate === null) throw Error(k(340));
        on();
      }
      return (
        (e = t.flags), e & 65536 ? ((t.flags = (e & -65537) | 128), t) : null
      );
    case 19:
      return j(A), null;
    case 4:
      return sn(), null;
    case 10:
      return Di(t.type._context), null;
    case 22:
    case 23:
      return Zi(), null;
    case 24:
      return null;
    default:
      return null;
  }
}
var Nr = !1,
  le = !1,
  mp = typeof WeakSet == "function" ? WeakSet : Set,
  C = null;
function Zt(e, t) {
  var n = e.ref;
  if (n !== null)
    if (typeof n == "function")
      try {
        n(null);
      } catch (r) {
        W(e, t, r);
      }
    else n.current = null;
}
function qu(e, t, n) {
  try {
    n();
  } catch (r) {
    W(e, t, r);
  }
}
var ca = !1;
function yp(e, t) {
  if (((Fu = qr), (e = Ls()), Li(e))) {
    if ("selectionStart" in e)
      var n = { start: e.selectionStart, end: e.selectionEnd };
    else
      e: {
        n = ((n = e.ownerDocument) && n.defaultView) || window;
        var r = n.getSelection && n.getSelection();
        if (r && r.rangeCount !== 0) {
          n = r.anchorNode;
          var l = r.anchorOffset,
            u = r.focusNode;
          r = r.focusOffset;
          try {
            n.nodeType, u.nodeType;
          } catch {
            n = null;
            break e;
          }
          var i = 0,
            o = -1,
            a = -1,
            s = 0,
            h = 0,
            m = e,
            p = null;
          t: for (;;) {
            for (
              var y;
              m !== n || (l !== 0 && m.nodeType !== 3) || (o = i + l),
                m !== u || (r !== 0 && m.nodeType !== 3) || (a = i + r),
                m.nodeType === 3 && (i += m.nodeValue.length),
                (y = m.firstChild) !== null;

            )
              (p = m), (m = y);
            for (;;) {
              if (m === e) break t;
              if (
                (p === n && ++s === l && (o = i),
                p === u && ++h === r && (a = i),
                (y = m.nextSibling) !== null)
              )
                break;
              (m = p), (p = m.parentNode);
            }
            m = y;
          }
          n = o === -1 || a === -1 ? null : { start: o, end: a };
        } else n = null;
      }
    n = n || { start: 0, end: 0 };
  } else n = null;
  for (Mu = { focusedElem: e, selectionRange: n }, qr = !1, C = t; C !== null; )
    if (((t = C), (e = t.child), (t.subtreeFlags & 1028) !== 0 && e !== null))
      (e.return = t), (C = e);
    else
      for (; C !== null; ) {
        t = C;
        try {
          var S = t.alternate;
          if (t.flags & 1024)
            switch (t.tag) {
              case 0:
              case 11:
              case 15:
                break;
              case 1:
                if (S !== null) {
                  var g = S.memoizedProps,
                    x = S.memoizedState,
                    f = t.stateNode,
                    c = f.getSnapshotBeforeUpdate(
                      t.elementType === t.type ? g : Ne(t.type, g),
                      x
                    );
                  f.__reactInternalSnapshotBeforeUpdate = c;
                }
                break;
              case 3:
                var d = t.stateNode.containerInfo;
                d.nodeType === 1
                  ? (d.textContent = "")
                  : d.nodeType === 9 &&
                    d.documentElement &&
                    d.removeChild(d.documentElement);
                break;
              case 5:
              case 6:
              case 4:
              case 17:
                break;
              default:
                throw Error(k(163));
            }
        } catch (w) {
          W(t, t.return, w);
        }
        if (((e = t.sibling), e !== null)) {
          (e.return = t.return), (C = e);
          break;
        }
        C = t.return;
      }
  return (S = ca), (ca = !1), S;
}
function jn(e, t, n) {
  var r = t.updateQueue;
  if (((r = r !== null ? r.lastEffect : null), r !== null)) {
    var l = (r = r.next);
    do {
      if ((l.tag & e) === e) {
        var u = l.destroy;
        (l.destroy = void 0), u !== void 0 && qu(t, n, u);
      }
      l = l.next;
    } while (l !== r);
  }
}
function Rl(e, t) {
  if (
    ((t = t.updateQueue), (t = t !== null ? t.lastEffect : null), t !== null)
  ) {
    var n = (t = t.next);
    do {
      if ((n.tag & e) === e) {
        var r = n.create;
        n.destroy = r();
      }
      n = n.next;
    } while (n !== t);
  }
}
function bu(e) {
  var t = e.ref;
  if (t !== null) {
    var n = e.stateNode;
    switch (e.tag) {
      case 5:
        e = n;
        break;
      default:
        e = n;
    }
    typeof t == "function" ? t(e) : (t.current = e);
  }
}
function Cc(e) {
  var t = e.alternate;
  t !== null && ((e.alternate = null), Cc(t)),
    (e.child = null),
    (e.deletions = null),
    (e.sibling = null),
    e.tag === 5 &&
      ((t = e.stateNode),
      t !== null &&
        (delete t[je], delete t[Jn], delete t[Uu], delete t[qd], delete t[bd])),
    (e.stateNode = null),
    (e.return = null),
    (e.dependencies = null),
    (e.memoizedProps = null),
    (e.memoizedState = null),
    (e.pendingProps = null),
    (e.stateNode = null),
    (e.updateQueue = null);
}
function Pc(e) {
  return e.tag === 5 || e.tag === 3 || e.tag === 4;
}
function fa(e) {
  e: for (;;) {
    for (; e.sibling === null; ) {
      if (e.return === null || Pc(e.return)) return null;
      e = e.return;
    }
    for (
      e.sibling.return = e.return, e = e.sibling;
      e.tag !== 5 && e.tag !== 6 && e.tag !== 18;

    ) {
      if (e.flags & 2 || e.child === null || e.tag === 4) continue e;
      (e.child.return = e), (e = e.child);
    }
    if (!(e.flags & 2)) return e.stateNode;
  }
}
function ei(e, t, n) {
  var r = e.tag;
  if (r === 5 || r === 6)
    (e = e.stateNode),
      t
        ? n.nodeType === 8
          ? n.parentNode.insertBefore(e, t)
          : n.insertBefore(e, t)
        : (n.nodeType === 8
            ? ((t = n.parentNode), t.insertBefore(e, n))
            : ((t = n), t.appendChild(e)),
          (n = n._reactRootContainer),
          n != null || t.onclick !== null || (t.onclick = tl));
  else if (r !== 4 && ((e = e.child), e !== null))
    for (ei(e, t, n), e = e.sibling; e !== null; ) ei(e, t, n), (e = e.sibling);
}
function ti(e, t, n) {
  var r = e.tag;
  if (r === 5 || r === 6)
    (e = e.stateNode), t ? n.insertBefore(e, t) : n.appendChild(e);
  else if (r !== 4 && ((e = e.child), e !== null))
    for (ti(e, t, n), e = e.sibling; e !== null; ) ti(e, t, n), (e = e.sibling);
}
var b = null,
  Te = !1;
function tt(e, t, n) {
  for (n = n.child; n !== null; ) _c(e, t, n), (n = n.sibling);
}
function _c(e, t, n) {
  if (Ue && typeof Ue.onCommitFiberUnmount == "function")
    try {
      Ue.onCommitFiberUnmount(wl, n);
    } catch {}
  switch (n.tag) {
    case 5:
      le || Zt(n, t);
    case 6:
      var r = b,
        l = Te;
      (b = null),
        tt(e, t, n),
        (b = r),
        (Te = l),
        b !== null &&
          (Te
            ? ((e = b),
              (n = n.stateNode),
              e.nodeType === 8 ? e.parentNode.removeChild(n) : e.removeChild(n))
            : b.removeChild(n.stateNode));
      break;
    case 18:
      b !== null &&
        (Te
          ? ((e = b),
            (n = n.stateNode),
            e.nodeType === 8
              ? tu(e.parentNode, n)
              : e.nodeType === 1 && tu(e, n),
            Kn(e))
          : tu(b, n.stateNode));
      break;
    case 4:
      (r = b),
        (l = Te),
        (b = n.stateNode.containerInfo),
        (Te = !0),
        tt(e, t, n),
        (b = r),
        (Te = l);
      break;
    case 0:
    case 11:
    case 14:
    case 15:
      if (
        !le &&
        ((r = n.updateQueue), r !== null && ((r = r.lastEffect), r !== null))
      ) {
        l = r = r.next;
        do {
          var u = l,
            i = u.destroy;
          (u = u.tag),
            i !== void 0 && (u & 2 || u & 4) && qu(n, t, i),
            (l = l.next);
        } while (l !== r);
      }
      tt(e, t, n);
      break;
    case 1:
      if (
        !le &&
        (Zt(n, t),
        (r = n.stateNode),
        typeof r.componentWillUnmount == "function")
      )
        try {
          (r.props = n.memoizedProps),
            (r.state = n.memoizedState),
            r.componentWillUnmount();
        } catch (o) {
          W(n, t, o);
        }
      tt(e, t, n);
      break;
    case 21:
      tt(e, t, n);
      break;
    case 22:
      n.mode & 1
        ? ((le = (r = le) || n.memoizedState !== null), tt(e, t, n), (le = r))
        : tt(e, t, n);
      break;
    default:
      tt(e, t, n);
  }
}
function da(e) {
  var t = e.updateQueue;
  if (t !== null) {
    e.updateQueue = null;
    var n = e.stateNode;
    n === null && (n = e.stateNode = new mp()),
      t.forEach(function (r) {
        var l = Pp.bind(null, e, r);
        n.has(r) || (n.add(r), r.then(l, l));
      });
  }
}
function Le(e, t) {
  var n = t.deletions;
  if (n !== null)
    for (var r = 0; r < n.length; r++) {
      var l = n[r];
      try {
        var u = e,
          i = t,
          o = i;
        e: for (; o !== null; ) {
          switch (o.tag) {
            case 5:
              (b = o.stateNode), (Te = !1);
              break e;
            case 3:
              (b = o.stateNode.containerInfo), (Te = !0);
              break e;
            case 4:
              (b = o.stateNode.containerInfo), (Te = !0);
              break e;
          }
          o = o.return;
        }
        if (b === null) throw Error(k(160));
        _c(u, i, l), (b = null), (Te = !1);
        var a = l.alternate;
        a !== null && (a.return = null), (l.return = null);
      } catch (s) {
        W(l, t, s);
      }
    }
  if (t.subtreeFlags & 12854)
    for (t = t.child; t !== null; ) Rc(t, e), (t = t.sibling);
}
function Rc(e, t) {
  var n = e.alternate,
    r = e.flags;
  switch (e.tag) {
    case 0:
    case 11:
    case 14:
    case 15:
      if ((Le(t, e), Me(e), r & 4)) {
        try {
          jn(3, e, e.return), Rl(3, e);
        } catch (g) {
          W(e, e.return, g);
        }
        try {
          jn(5, e, e.return);
        } catch (g) {
          W(e, e.return, g);
        }
      }
      break;
    case 1:
      Le(t, e), Me(e), r & 512 && n !== null && Zt(n, n.return);
      break;
    case 5:
      if (
        (Le(t, e),
        Me(e),
        r & 512 && n !== null && Zt(n, n.return),
        e.flags & 32)
      ) {
        var l = e.stateNode;
        try {
          Hn(l, "");
        } catch (g) {
          W(e, e.return, g);
        }
      }
      if (r & 4 && ((l = e.stateNode), l != null)) {
        var u = e.memoizedProps,
          i = n !== null ? n.memoizedProps : u,
          o = e.type,
          a = e.updateQueue;
        if (((e.updateQueue = null), a !== null))
          try {
            o === "input" && u.type === "radio" && u.name != null && Ga(l, u),
              Pu(o, i);
            var s = Pu(o, u);
            for (i = 0; i < a.length; i += 2) {
              var h = a[i],
                m = a[i + 1];
              h === "style"
                ? es(l, m)
                : h === "dangerouslySetInnerHTML"
                ? qa(l, m)
                : h === "children"
                ? Hn(l, m)
                : mi(l, h, m, s);
            }
            switch (o) {
              case "input":
                Su(l, u);
                break;
              case "textarea":
                Za(l, u);
                break;
              case "select":
                var p = l._wrapperState.wasMultiple;
                l._wrapperState.wasMultiple = !!u.multiple;
                var y = u.value;
                y != null
                  ? qt(l, !!u.multiple, y, !1)
                  : p !== !!u.multiple &&
                    (u.defaultValue != null
                      ? qt(l, !!u.multiple, u.defaultValue, !0)
                      : qt(l, !!u.multiple, u.multiple ? [] : "", !1));
            }
            l[Jn] = u;
          } catch (g) {
            W(e, e.return, g);
          }
      }
      break;
    case 6:
      if ((Le(t, e), Me(e), r & 4)) {
        if (e.stateNode === null) throw Error(k(162));
        (l = e.stateNode), (u = e.memoizedProps);
        try {
          l.nodeValue = u;
        } catch (g) {
          W(e, e.return, g);
        }
      }
      break;
    case 3:
      if (
        (Le(t, e), Me(e), r & 4 && n !== null && n.memoizedState.isDehydrated)
      )
        try {
          Kn(t.containerInfo);
        } catch (g) {
          W(e, e.return, g);
        }
      break;
    case 4:
      Le(t, e), Me(e);
      break;
    case 13:
      Le(t, e),
        Me(e),
        (l = e.child),
        l.flags & 8192 &&
          ((u = l.memoizedState !== null),
          (l.stateNode.isHidden = u),
          !u ||
            (l.alternate !== null && l.alternate.memoizedState !== null) ||
            (Xi = K())),
        r & 4 && da(e);
      break;
    case 22:
      if (
        ((h = n !== null && n.memoizedState !== null),
        e.mode & 1 ? ((le = (s = le) || h), Le(t, e), (le = s)) : Le(t, e),
        Me(e),
        r & 8192)
      ) {
        if (
          ((s = e.memoizedState !== null),
          (e.stateNode.isHidden = s) && !h && e.mode & 1)
        )
          for (C = e, h = e.child; h !== null; ) {
            for (m = C = h; C !== null; ) {
              switch (((p = C), (y = p.child), p.tag)) {
                case 0:
                case 11:
                case 14:
                case 15:
                  jn(4, p, p.return);
                  break;
                case 1:
                  Zt(p, p.return);
                  var S = p.stateNode;
                  if (typeof S.componentWillUnmount == "function") {
                    (r = p), (n = p.return);
                    try {
                      (t = r),
                        (S.props = t.memoizedProps),
                        (S.state = t.memoizedState),
                        S.componentWillUnmount();
                    } catch (g) {
                      W(r, n, g);
                    }
                  }
                  break;
                case 5:
                  Zt(p, p.return);
                  break;
                case 22:
                  if (p.memoizedState !== null) {
                    ha(m);
                    continue;
                  }
              }
              y !== null ? ((y.return = p), (C = y)) : ha(m);
            }
            h = h.sibling;
          }
        e: for (h = null, m = e; ; ) {
          if (m.tag === 5) {
            if (h === null) {
              h = m;
              try {
                (l = m.stateNode),
                  s
                    ? ((u = l.style),
                      typeof u.setProperty == "function"
                        ? u.setProperty("display", "none", "important")
                        : (u.display = "none"))
                    : ((o = m.stateNode),
                      (a = m.memoizedProps.style),
                      (i =
                        a != null && a.hasOwnProperty("display")
                          ? a.display
                          : null),
                      (o.style.display = ba("display", i)));
              } catch (g) {
                W(e, e.return, g);
              }
            }
          } else if (m.tag === 6) {
            if (h === null)
              try {
                m.stateNode.nodeValue = s ? "" : m.memoizedProps;
              } catch (g) {
                W(e, e.return, g);
              }
          } else if (
            ((m.tag !== 22 && m.tag !== 23) ||
              m.memoizedState === null ||
              m === e) &&
            m.child !== null
          ) {
            (m.child.return = m), (m = m.child);
            continue;
          }
          if (m === e) break e;
          for (; m.sibling === null; ) {
            if (m.return === null || m.return === e) break e;
            h === m && (h = null), (m = m.return);
          }
          h === m && (h = null), (m.sibling.return = m.return), (m = m.sibling);
        }
      }
      break;
    case 19:
      Le(t, e), Me(e), r & 4 && da(e);
      break;
    case 21:
      break;
    default:
      Le(t, e), Me(e);
  }
}
function Me(e) {
  var t = e.flags;
  if (t & 2) {
    try {
      e: {
        for (var n = e.return; n !== null; ) {
          if (Pc(n)) {
            var r = n;
            break e;
          }
          n = n.return;
        }
        throw Error(k(160));
      }
      switch (r.tag) {
        case 5:
          var l = r.stateNode;
          r.flags & 32 && (Hn(l, ""), (r.flags &= -33));
          var u = fa(e);
          ti(e, u, l);
          break;
        case 3:
        case 4:
          var i = r.stateNode.containerInfo,
            o = fa(e);
          ei(e, o, i);
          break;
        default:
          throw Error(k(161));
      }
    } catch (a) {
      W(e, e.return, a);
    }
    e.flags &= -3;
  }
  t & 4096 && (e.flags &= -4097);
}
function vp(e, t, n) {
  (C = e), Lc(e);
}
function Lc(e, t, n) {
  for (var r = (e.mode & 1) !== 0; C !== null; ) {
    var l = C,
      u = l.child;
    if (l.tag === 22 && r) {
      var i = l.memoizedState !== null || Nr;
      if (!i) {
        var o = l.alternate,
          a = (o !== null && o.memoizedState !== null) || le;
        o = Nr;
        var s = le;
        if (((Nr = i), (le = a) && !s))
          for (C = l; C !== null; )
            (i = C),
              (a = i.child),
              i.tag === 22 && i.memoizedState !== null
                ? ma(l)
                : a !== null
                ? ((a.return = i), (C = a))
                : ma(l);
        for (; u !== null; ) (C = u), Lc(u), (u = u.sibling);
        (C = l), (Nr = o), (le = s);
      }
      pa(e);
    } else
      l.subtreeFlags & 8772 && u !== null ? ((u.return = l), (C = u)) : pa(e);
  }
}
function pa(e) {
  for (; C !== null; ) {
    var t = C;
    if (t.flags & 8772) {
      var n = t.alternate;
      try {
        if (t.flags & 8772)
          switch (t.tag) {
            case 0:
            case 11:
            case 15:
              le || Rl(5, t);
              break;
            case 1:
              var r = t.stateNode;
              if (t.flags & 4 && !le)
                if (n === null) r.componentDidMount();
                else {
                  var l =
                    t.elementType === t.type
                      ? n.memoizedProps
                      : Ne(t.type, n.memoizedProps);
                  r.componentDidUpdate(
                    l,
                    n.memoizedState,
                    r.__reactInternalSnapshotBeforeUpdate
                  );
                }
              var u = t.updateQueue;
              u !== null && Jo(t, u, r);
              break;
            case 3:
              var i = t.updateQueue;
              if (i !== null) {
                if (((n = null), t.child !== null))
                  switch (t.child.tag) {
                    case 5:
                      n = t.child.stateNode;
                      break;
                    case 1:
                      n = t.child.stateNode;
                  }
                Jo(t, i, n);
              }
              break;
            case 5:
              var o = t.stateNode;
              if (n === null && t.flags & 4) {
                n = o;
                var a = t.memoizedProps;
                switch (t.type) {
                  case "button":
                  case "input":
                  case "select":
                  case "textarea":
                    a.autoFocus && n.focus();
                    break;
                  case "img":
                    a.src && (n.src = a.src);
                }
              }
              break;
            case 6:
              break;
            case 4:
              break;
            case 12:
              break;
            case 13:
              if (t.memoizedState === null) {
                var s = t.alternate;
                if (s !== null) {
                  var h = s.memoizedState;
                  if (h !== null) {
                    var m = h.dehydrated;
                    m !== null && Kn(m);
                  }
                }
              }
              break;
            case 19:
            case 17:
            case 21:
            case 22:
            case 23:
            case 25:
              break;
            default:
              throw Error(k(163));
          }
        le || (t.flags & 512 && bu(t));
      } catch (p) {
        W(t, t.return, p);
      }
    }
    if (t === e) {
      C = null;
      break;
    }
    if (((n = t.sibling), n !== null)) {
      (n.return = t.return), (C = n);
      break;
    }
    C = t.return;
  }
}
function ha(e) {
  for (; C !== null; ) {
    var t = C;
    if (t === e) {
      C = null;
      break;
    }
    var n = t.sibling;
    if (n !== null) {
      (n.return = t.return), (C = n);
      break;
    }
    C = t.return;
  }
}
function ma(e) {
  for (; C !== null; ) {
    var t = C;
    try {
      switch (t.tag) {
        case 0:
        case 11:
        case 15:
          var n = t.return;
          try {
            Rl(4, t);
          } catch (a) {
            W(t, n, a);
          }
          break;
        case 1:
          var r = t.stateNode;
          if (typeof r.componentDidMount == "function") {
            var l = t.return;
            try {
              r.componentDidMount();
            } catch (a) {
              W(t, l, a);
            }
          }
          var u = t.return;
          try {
            bu(t);
          } catch (a) {
            W(t, u, a);
          }
          break;
        case 5:
          var i = t.return;
          try {
            bu(t);
          } catch (a) {
            W(t, i, a);
          }
      }
    } catch (a) {
      W(t, t.return, a);
    }
    if (t === e) {
      C = null;
      break;
    }
    var o = t.sibling;
    if (o !== null) {
      (o.return = t.return), (C = o);
      break;
    }
    C = t.return;
  }
}
var gp = Math.ceil,
  dl = be.ReactCurrentDispatcher,
  Ki = be.ReactCurrentOwner,
  Pe = be.ReactCurrentBatchConfig,
  D = 0,
  q = null,
  Y = null,
  ee = 0,
  ye = 0,
  Jt = St(0),
  G = 0,
  rr = null,
  Ft = 0,
  Ll = 0,
  Yi = 0,
  Un = null,
  fe = null,
  Xi = 0,
  fn = 1 / 0,
  He = null,
  pl = !1,
  ni = null,
  pt = null,
  Tr = !1,
  ot = null,
  hl = 0,
  An = 0,
  ri = null,
  Hr = -1,
  Vr = 0;
function oe() {
  return D & 6 ? K() : Hr !== -1 ? Hr : (Hr = K());
}
function ht(e) {
  return e.mode & 1
    ? D & 2 && ee !== 0
      ? ee & -ee
      : tp.transition !== null
      ? (Vr === 0 && (Vr = ds()), Vr)
      : ((e = F),
        e !== 0 || ((e = window.event), (e = e === void 0 ? 16 : ws(e.type))),
        e)
    : 1;
}
function Ie(e, t, n, r) {
  if (50 < An) throw ((An = 0), (ri = null), Error(k(185)));
  ir(e, n, r),
    (!(D & 2) || e !== q) &&
      (e === q && (!(D & 2) && (Ll |= n), G === 4 && ut(e, ee)),
      me(e, r),
      n === 1 && D === 0 && !(t.mode & 1) && ((fn = K() + 500), Cl && kt()));
}
function me(e, t) {
  var n = e.callbackNode;
  td(e, t);
  var r = Jr(e, e === q ? ee : 0);
  if (r === 0)
    n !== null && Co(n), (e.callbackNode = null), (e.callbackPriority = 0);
  else if (((t = r & -r), e.callbackPriority !== t)) {
    if ((n != null && Co(n), t === 1))
      e.tag === 0 ? ep(ya.bind(null, e)) : js(ya.bind(null, e)),
        Zd(function () {
          !(D & 6) && kt();
        }),
        (n = null);
    else {
      switch (ps(r)) {
        case 1:
          n = Si;
          break;
        case 4:
          n = cs;
          break;
        case 16:
          n = Zr;
          break;
        case 536870912:
          n = fs;
          break;
        default:
          n = Zr;
      }
      n = Mc(n, Nc.bind(null, e));
    }
    (e.callbackPriority = t), (e.callbackNode = n);
  }
}
function Nc(e, t) {
  if (((Hr = -1), (Vr = 0), D & 6)) throw Error(k(327));
  var n = e.callbackNode;
  if (rn() && e.callbackNode !== n) return null;
  var r = Jr(e, e === q ? ee : 0);
  if (r === 0) return null;
  if (r & 30 || r & e.expiredLanes || t) t = ml(e, r);
  else {
    t = r;
    var l = D;
    D |= 2;
    var u = zc();
    (q !== e || ee !== t) && ((He = null), (fn = K() + 500), Tt(e, t));
    do
      try {
        kp();
        break;
      } catch (o) {
        Tc(e, o);
      }
    while (!0);
    Ii(),
      (dl.current = u),
      (D = l),
      Y !== null ? (t = 0) : ((q = null), (ee = 0), (t = G));
  }
  if (t !== 0) {
    if (
      (t === 2 && ((l = Tu(e)), l !== 0 && ((r = l), (t = li(e, l)))), t === 1)
    )
      throw ((n = rr), Tt(e, 0), ut(e, r), me(e, K()), n);
    if (t === 6) ut(e, r);
    else {
      if (
        ((l = e.current.alternate),
        !(r & 30) &&
          !wp(l) &&
          ((t = ml(e, r)),
          t === 2 && ((u = Tu(e)), u !== 0 && ((r = u), (t = li(e, u)))),
          t === 1))
      )
        throw ((n = rr), Tt(e, 0), ut(e, r), me(e, K()), n);
      switch (((e.finishedWork = l), (e.finishedLanes = r), t)) {
        case 0:
        case 1:
          throw Error(k(345));
        case 2:
          _t(e, fe, He);
          break;
        case 3:
          if (
            (ut(e, r), (r & 130023424) === r && ((t = Xi + 500 - K()), 10 < t))
          ) {
            if (Jr(e, 0) !== 0) break;
            if (((l = e.suspendedLanes), (l & r) !== r)) {
              oe(), (e.pingedLanes |= e.suspendedLanes & l);
              break;
            }
            e.timeoutHandle = ju(_t.bind(null, e, fe, He), t);
            break;
          }
          _t(e, fe, He);
          break;
        case 4:
          if ((ut(e, r), (r & 4194240) === r)) break;
          for (t = e.eventTimes, l = -1; 0 < r; ) {
            var i = 31 - Oe(r);
            (u = 1 << i), (i = t[i]), i > l && (l = i), (r &= ~u);
          }
          if (
            ((r = l),
            (r = K() - r),
            (r =
              (120 > r
                ? 120
                : 480 > r
                ? 480
                : 1080 > r
                ? 1080
                : 1920 > r
                ? 1920
                : 3e3 > r
                ? 3e3
                : 4320 > r
                ? 4320
                : 1960 * gp(r / 1960)) - r),
            10 < r)
          ) {
            e.timeoutHandle = ju(_t.bind(null, e, fe, He), r);
            break;
          }
          _t(e, fe, He);
          break;
        case 5:
          _t(e, fe, He);
          break;
        default:
          throw Error(k(329));
      }
    }
  }
  return me(e, K()), e.callbackNode === n ? Nc.bind(null, e) : null;
}
function li(e, t) {
  var n = Un;
  return (
    e.current.memoizedState.isDehydrated && (Tt(e, t).flags |= 256),
    (e = ml(e, t)),
    e !== 2 && ((t = fe), (fe = n), t !== null && ui(t)),
    e
  );
}
function ui(e) {
  fe === null ? (fe = e) : fe.push.apply(fe, e);
}
function wp(e) {
  for (var t = e; ; ) {
    if (t.flags & 16384) {
      var n = t.updateQueue;
      if (n !== null && ((n = n.stores), n !== null))
        for (var r = 0; r < n.length; r++) {
          var l = n[r],
            u = l.getSnapshot;
          l = l.value;
          try {
            if (!De(u(), l)) return !1;
          } catch {
            return !1;
          }
        }
    }
    if (((n = t.child), t.subtreeFlags & 16384 && n !== null))
      (n.return = t), (t = n);
    else {
      if (t === e) break;
      for (; t.sibling === null; ) {
        if (t.return === null || t.return === e) return !0;
        t = t.return;
      }
      (t.sibling.return = t.return), (t = t.sibling);
    }
  }
  return !0;
}
function ut(e, t) {
  for (
    t &= ~Yi,
      t &= ~Ll,
      e.suspendedLanes |= t,
      e.pingedLanes &= ~t,
      e = e.expirationTimes;
    0 < t;

  ) {
    var n = 31 - Oe(t),
      r = 1 << n;
    (e[n] = -1), (t &= ~r);
  }
}
function ya(e) {
  if (D & 6) throw Error(k(327));
  rn();
  var t = Jr(e, 0);
  if (!(t & 1)) return me(e, K()), null;
  var n = ml(e, t);
  if (e.tag !== 0 && n === 2) {
    var r = Tu(e);
    r !== 0 && ((t = r), (n = li(e, r)));
  }
  if (n === 1) throw ((n = rr), Tt(e, 0), ut(e, t), me(e, K()), n);
  if (n === 6) throw Error(k(345));
  return (
    (e.finishedWork = e.current.alternate),
    (e.finishedLanes = t),
    _t(e, fe, He),
    me(e, K()),
    null
  );
}
function Gi(e, t) {
  var n = D;
  D |= 1;
  try {
    return e(t);
  } finally {
    (D = n), D === 0 && ((fn = K() + 500), Cl && kt());
  }
}
function Mt(e) {
  ot !== null && ot.tag === 0 && !(D & 6) && rn();
  var t = D;
  D |= 1;
  var n = Pe.transition,
    r = F;
  try {
    if (((Pe.transition = null), (F = 1), e)) return e();
  } finally {
    (F = r), (Pe.transition = n), (D = t), !(D & 6) && kt();
  }
}
function Zi() {
  (ye = Jt.current), j(Jt);
}
function Tt(e, t) {
  (e.finishedWork = null), (e.finishedLanes = 0);
  var n = e.timeoutHandle;
  if ((n !== -1 && ((e.timeoutHandle = -1), Gd(n)), Y !== null))
    for (n = Y.return; n !== null; ) {
      var r = n;
      switch ((Ti(r), r.tag)) {
        case 1:
          (r = r.type.childContextTypes), r != null && nl();
          break;
        case 3:
          sn(), j(pe), j(ue), Ui();
          break;
        case 5:
          ji(r);
          break;
        case 4:
          sn();
          break;
        case 13:
          j(A);
          break;
        case 19:
          j(A);
          break;
        case 10:
          Di(r.type._context);
          break;
        case 22:
        case 23:
          Zi();
      }
      n = n.return;
    }
  if (
    ((q = e),
    (Y = e = mt(e.current, null)),
    (ee = ye = t),
    (G = 0),
    (rr = null),
    (Yi = Ll = Ft = 0),
    (fe = Un = null),
    Lt !== null)
  ) {
    for (t = 0; t < Lt.length; t++)
      if (((n = Lt[t]), (r = n.interleaved), r !== null)) {
        n.interleaved = null;
        var l = r.next,
          u = n.pending;
        if (u !== null) {
          var i = u.next;
          (u.next = l), (r.next = i);
        }
        n.pending = r;
      }
    Lt = null;
  }
  return e;
}
function Tc(e, t) {
  do {
    var n = Y;
    try {
      if ((Ii(), (Ur.current = fl), cl)) {
        for (var r = B.memoizedState; r !== null; ) {
          var l = r.queue;
          l !== null && (l.pending = null), (r = r.next);
        }
        cl = !1;
      }
      if (
        ((Dt = 0),
        (J = X = B = null),
        ($n = !1),
        (er = 0),
        (Ki.current = null),
        n === null || n.return === null)
      ) {
        (G = 1), (rr = t), (Y = null);
        break;
      }
      e: {
        var u = e,
          i = n.return,
          o = n,
          a = t;
        if (
          ((t = ee),
          (o.flags |= 32768),
          a !== null && typeof a == "object" && typeof a.then == "function")
        ) {
          var s = a,
            h = o,
            m = h.tag;
          if (!(h.mode & 1) && (m === 0 || m === 11 || m === 15)) {
            var p = h.alternate;
            p
              ? ((h.updateQueue = p.updateQueue),
                (h.memoizedState = p.memoizedState),
                (h.lanes = p.lanes))
              : ((h.updateQueue = null), (h.memoizedState = null));
          }
          var y = ra(i);
          if (y !== null) {
            (y.flags &= -257),
              la(y, i, o, u, t),
              y.mode & 1 && na(u, s, t),
              (t = y),
              (a = s);
            var S = t.updateQueue;
            if (S === null) {
              var g = new Set();
              g.add(a), (t.updateQueue = g);
            } else S.add(a);
            break e;
          } else {
            if (!(t & 1)) {
              na(u, s, t), Ji();
              break e;
            }
            a = Error(k(426));
          }
        } else if (U && o.mode & 1) {
          var x = ra(i);
          if (x !== null) {
            !(x.flags & 65536) && (x.flags |= 256),
              la(x, i, o, u, t),
              zi(cn(a, o));
            break e;
          }
        }
        (u = a = cn(a, o)),
          G !== 4 && (G = 2),
          Un === null ? (Un = [u]) : Un.push(u),
          (u = i);
        do {
          switch (u.tag) {
            case 3:
              (u.flags |= 65536), (t &= -t), (u.lanes |= t);
              var f = pc(u, a, t);
              Zo(u, f);
              break e;
            case 1:
              o = a;
              var c = u.type,
                d = u.stateNode;
              if (
                !(u.flags & 128) &&
                (typeof c.getDerivedStateFromError == "function" ||
                  (d !== null &&
                    typeof d.componentDidCatch == "function" &&
                    (pt === null || !pt.has(d))))
              ) {
                (u.flags |= 65536), (t &= -t), (u.lanes |= t);
                var w = hc(u, o, t);
                Zo(u, w);
                break e;
              }
          }
          u = u.return;
        } while (u !== null);
      }
      Ic(n);
    } catch (E) {
      (t = E), Y === n && n !== null && (Y = n = n.return);
      continue;
    }
    break;
  } while (!0);
}
function zc() {
  var e = dl.current;
  return (dl.current = fl), e === null ? fl : e;
}
function Ji() {
  (G === 0 || G === 3 || G === 2) && (G = 4),
    q === null || (!(Ft & 268435455) && !(Ll & 268435455)) || ut(q, ee);
}
function ml(e, t) {
  var n = D;
  D |= 2;
  var r = zc();
  (q !== e || ee !== t) && ((He = null), Tt(e, t));
  do
    try {
      Sp();
      break;
    } catch (l) {
      Tc(e, l);
    }
  while (!0);
  if ((Ii(), (D = n), (dl.current = r), Y !== null)) throw Error(k(261));
  return (q = null), (ee = 0), G;
}
function Sp() {
  for (; Y !== null; ) Oc(Y);
}
function kp() {
  for (; Y !== null && !Kf(); ) Oc(Y);
}
function Oc(e) {
  var t = Fc(e.alternate, e, ye);
  (e.memoizedProps = e.pendingProps),
    t === null ? Ic(e) : (Y = t),
    (Ki.current = null);
}
function Ic(e) {
  var t = e;
  do {
    var n = t.alternate;
    if (((e = t.return), t.flags & 32768)) {
      if (((n = hp(n, t)), n !== null)) {
        (n.flags &= 32767), (Y = n);
        return;
      }
      if (e !== null)
        (e.flags |= 32768), (e.subtreeFlags = 0), (e.deletions = null);
      else {
        (G = 6), (Y = null);
        return;
      }
    } else if (((n = pp(n, t, ye)), n !== null)) {
      Y = n;
      return;
    }
    if (((t = t.sibling), t !== null)) {
      Y = t;
      return;
    }
    Y = t = e;
  } while (t !== null);
  G === 0 && (G = 5);
}
function _t(e, t, n) {
  var r = F,
    l = Pe.transition;
  try {
    (Pe.transition = null), (F = 1), Ep(e, t, n, r);
  } finally {
    (Pe.transition = l), (F = r);
  }
  return null;
}
function Ep(e, t, n, r) {
  do rn();
  while (ot !== null);
  if (D & 6) throw Error(k(327));
  n = e.finishedWork;
  var l = e.finishedLanes;
  if (n === null) return null;
  if (((e.finishedWork = null), (e.finishedLanes = 0), n === e.current))
    throw Error(k(177));
  (e.callbackNode = null), (e.callbackPriority = 0);
  var u = n.lanes | n.childLanes;
  if (
    (nd(e, u),
    e === q && ((Y = q = null), (ee = 0)),
    (!(n.subtreeFlags & 2064) && !(n.flags & 2064)) ||
      Tr ||
      ((Tr = !0),
      Mc(Zr, function () {
        return rn(), null;
      })),
    (u = (n.flags & 15990) !== 0),
    n.subtreeFlags & 15990 || u)
  ) {
    (u = Pe.transition), (Pe.transition = null);
    var i = F;
    F = 1;
    var o = D;
    (D |= 4),
      (Ki.current = null),
      yp(e, n),
      Rc(n, e),
      Hd(Mu),
      (qr = !!Fu),
      (Mu = Fu = null),
      (e.current = n),
      vp(n),
      Yf(),
      (D = o),
      (F = i),
      (Pe.transition = u);
  } else e.current = n;
  if (
    (Tr && ((Tr = !1), (ot = e), (hl = l)),
    (u = e.pendingLanes),
    u === 0 && (pt = null),
    Zf(n.stateNode),
    me(e, K()),
    t !== null)
  )
    for (r = e.onRecoverableError, n = 0; n < t.length; n++)
      (l = t[n]), r(l.value, { componentStack: l.stack, digest: l.digest });
  if (pl) throw ((pl = !1), (e = ni), (ni = null), e);
  return (
    hl & 1 && e.tag !== 0 && rn(),
    (u = e.pendingLanes),
    u & 1 ? (e === ri ? An++ : ((An = 0), (ri = e))) : (An = 0),
    kt(),
    null
  );
}
function rn() {
  if (ot !== null) {
    var e = ps(hl),
      t = Pe.transition,
      n = F;
    try {
      if (((Pe.transition = null), (F = 16 > e ? 16 : e), ot === null))
        var r = !1;
      else {
        if (((e = ot), (ot = null), (hl = 0), D & 6)) throw Error(k(331));
        var l = D;
        for (D |= 4, C = e.current; C !== null; ) {
          var u = C,
            i = u.child;
          if (C.flags & 16) {
            var o = u.deletions;
            if (o !== null) {
              for (var a = 0; a < o.length; a++) {
                var s = o[a];
                for (C = s; C !== null; ) {
                  var h = C;
                  switch (h.tag) {
                    case 0:
                    case 11:
                    case 15:
                      jn(8, h, u);
                  }
                  var m = h.child;
                  if (m !== null) (m.return = h), (C = m);
                  else
                    for (; C !== null; ) {
                      h = C;
                      var p = h.sibling,
                        y = h.return;
                      if ((Cc(h), h === s)) {
                        C = null;
                        break;
                      }
                      if (p !== null) {
                        (p.return = y), (C = p);
                        break;
                      }
                      C = y;
                    }
                }
              }
              var S = u.alternate;
              if (S !== null) {
                var g = S.child;
                if (g !== null) {
                  S.child = null;
                  do {
                    var x = g.sibling;
                    (g.sibling = null), (g = x);
                  } while (g !== null);
                }
              }
              C = u;
            }
          }
          if (u.subtreeFlags & 2064 && i !== null) (i.return = u), (C = i);
          else
            e: for (; C !== null; ) {
              if (((u = C), u.flags & 2048))
                switch (u.tag) {
                  case 0:
                  case 11:
                  case 15:
                    jn(9, u, u.return);
                }
              var f = u.sibling;
              if (f !== null) {
                (f.return = u.return), (C = f);
                break e;
              }
              C = u.return;
            }
        }
        var c = e.current;
        for (C = c; C !== null; ) {
          i = C;
          var d = i.child;
          if (i.subtreeFlags & 2064 && d !== null) (d.return = i), (C = d);
          else
            e: for (i = c; C !== null; ) {
              if (((o = C), o.flags & 2048))
                try {
                  switch (o.tag) {
                    case 0:
                    case 11:
                    case 15:
                      Rl(9, o);
                  }
                } catch (E) {
                  W(o, o.return, E);
                }
              if (o === i) {
                C = null;
                break e;
              }
              var w = o.sibling;
              if (w !== null) {
                (w.return = o.return), (C = w);
                break e;
              }
              C = o.return;
            }
        }
        if (
          ((D = l), kt(), Ue && typeof Ue.onPostCommitFiberRoot == "function")
        )
          try {
            Ue.onPostCommitFiberRoot(wl, e);
          } catch {}
        r = !0;
      }
      return r;
    } finally {
      (F = n), (Pe.transition = t);
    }
  }
  return !1;
}
function va(e, t, n) {
  (t = cn(n, t)),
    (t = pc(e, t, 1)),
    (e = dt(e, t, 1)),
    (t = oe()),
    e !== null && (ir(e, 1, t), me(e, t));
}
function W(e, t, n) {
  if (e.tag === 3) va(e, e, n);
  else
    for (; t !== null; ) {
      if (t.tag === 3) {
        va(t, e, n);
        break;
      } else if (t.tag === 1) {
        var r = t.stateNode;
        if (
          typeof t.type.getDerivedStateFromError == "function" ||
          (typeof r.componentDidCatch == "function" &&
            (pt === null || !pt.has(r)))
        ) {
          (e = cn(n, e)),
            (e = hc(t, e, 1)),
            (t = dt(t, e, 1)),
            (e = oe()),
            t !== null && (ir(t, 1, e), me(t, e));
          break;
        }
      }
      t = t.return;
    }
}
function xp(e, t, n) {
  var r = e.pingCache;
  r !== null && r.delete(t),
    (t = oe()),
    (e.pingedLanes |= e.suspendedLanes & n),
    q === e &&
      (ee & n) === n &&
      (G === 4 || (G === 3 && (ee & 130023424) === ee && 500 > K() - Xi)
        ? Tt(e, 0)
        : (Yi |= n)),
    me(e, t);
}
function Dc(e, t) {
  t === 0 &&
    (e.mode & 1
      ? ((t = Sr), (Sr <<= 1), !(Sr & 130023424) && (Sr = 4194304))
      : (t = 1));
  var n = oe();
  (e = Je(e, t)), e !== null && (ir(e, t, n), me(e, n));
}
function Cp(e) {
  var t = e.memoizedState,
    n = 0;
  t !== null && (n = t.retryLane), Dc(e, n);
}
function Pp(e, t) {
  var n = 0;
  switch (e.tag) {
    case 13:
      var r = e.stateNode,
        l = e.memoizedState;
      l !== null && (n = l.retryLane);
      break;
    case 19:
      r = e.stateNode;
      break;
    default:
      throw Error(k(314));
  }
  r !== null && r.delete(t), Dc(e, n);
}
var Fc;
Fc = function (e, t, n) {
  if (e !== null)
    if (e.memoizedProps !== t.pendingProps || pe.current) de = !0;
    else {
      if (!(e.lanes & n) && !(t.flags & 128)) return (de = !1), dp(e, t, n);
      de = !!(e.flags & 131072);
    }
  else (de = !1), U && t.flags & 1048576 && Us(t, ul, t.index);
  switch (((t.lanes = 0), t.tag)) {
    case 2:
      var r = t.type;
      Br(e, t), (e = t.pendingProps);
      var l = un(t, ue.current);
      nn(t, n), (l = Bi(null, t, r, e, l, n));
      var u = Hi();
      return (
        (t.flags |= 1),
        typeof l == "object" &&
        l !== null &&
        typeof l.render == "function" &&
        l.$$typeof === void 0
          ? ((t.tag = 1),
            (t.memoizedState = null),
            (t.updateQueue = null),
            he(r) ? ((u = !0), rl(t)) : (u = !1),
            (t.memoizedState =
              l.state !== null && l.state !== void 0 ? l.state : null),
            Mi(t),
            (l.updater = _l),
            (t.stateNode = l),
            (l._reactInternals = t),
            Qu(t, r, e, n),
            (t = Xu(null, t, r, !0, u, n)))
          : ((t.tag = 0), U && u && Ni(t), ie(null, t, l, n), (t = t.child)),
        t
      );
    case 16:
      r = t.elementType;
      e: {
        switch (
          (Br(e, t),
          (e = t.pendingProps),
          (l = r._init),
          (r = l(r._payload)),
          (t.type = r),
          (l = t.tag = Rp(r)),
          (e = Ne(r, e)),
          l)
        ) {
          case 0:
            t = Yu(null, t, r, e, n);
            break e;
          case 1:
            t = oa(null, t, r, e, n);
            break e;
          case 11:
            t = ua(null, t, r, e, n);
            break e;
          case 14:
            t = ia(null, t, r, Ne(r.type, e), n);
            break e;
        }
        throw Error(k(306, r, ""));
      }
      return t;
    case 0:
      return (
        (r = t.type),
        (l = t.pendingProps),
        (l = t.elementType === r ? l : Ne(r, l)),
        Yu(e, t, r, l, n)
      );
    case 1:
      return (
        (r = t.type),
        (l = t.pendingProps),
        (l = t.elementType === r ? l : Ne(r, l)),
        oa(e, t, r, l, n)
      );
    case 3:
      e: {
        if ((gc(t), e === null)) throw Error(k(387));
        (r = t.pendingProps),
          (u = t.memoizedState),
          (l = u.element),
          Qs(e, t),
          al(t, r, null, n);
        var i = t.memoizedState;
        if (((r = i.element), u.isDehydrated))
          if (
            ((u = {
              element: r,
              isDehydrated: !1,
              cache: i.cache,
              pendingSuspenseBoundaries: i.pendingSuspenseBoundaries,
              transitions: i.transitions,
            }),
            (t.updateQueue.baseState = u),
            (t.memoizedState = u),
            t.flags & 256)
          ) {
            (l = cn(Error(k(423)), t)), (t = aa(e, t, r, n, l));
            break e;
          } else if (r !== l) {
            (l = cn(Error(k(424)), t)), (t = aa(e, t, r, n, l));
            break e;
          } else
            for (
              ve = ft(t.stateNode.containerInfo.firstChild),
                ge = t,
                U = !0,
                ze = null,
                n = Vs(t, null, r, n),
                t.child = n;
              n;

            )
              (n.flags = (n.flags & -3) | 4096), (n = n.sibling);
        else {
          if ((on(), r === l)) {
            t = qe(e, t, n);
            break e;
          }
          ie(e, t, r, n);
        }
        t = t.child;
      }
      return t;
    case 5:
      return (
        Ks(t),
        e === null && Hu(t),
        (r = t.type),
        (l = t.pendingProps),
        (u = e !== null ? e.memoizedProps : null),
        (i = l.children),
        $u(r, l) ? (i = null) : u !== null && $u(r, u) && (t.flags |= 32),
        vc(e, t),
        ie(e, t, i, n),
        t.child
      );
    case 6:
      return e === null && Hu(t), null;
    case 13:
      return wc(e, t, n);
    case 4:
      return (
        $i(t, t.stateNode.containerInfo),
        (r = t.pendingProps),
        e === null ? (t.child = an(t, null, r, n)) : ie(e, t, r, n),
        t.child
      );
    case 11:
      return (
        (r = t.type),
        (l = t.pendingProps),
        (l = t.elementType === r ? l : Ne(r, l)),
        ua(e, t, r, l, n)
      );
    case 7:
      return ie(e, t, t.pendingProps, n), t.child;
    case 8:
      return ie(e, t, t.pendingProps.children, n), t.child;
    case 12:
      return ie(e, t, t.pendingProps.children, n), t.child;
    case 10:
      e: {
        if (
          ((r = t.type._context),
          (l = t.pendingProps),
          (u = t.memoizedProps),
          (i = l.value),
          M(il, r._currentValue),
          (r._currentValue = i),
          u !== null)
        )
          if (De(u.value, i)) {
            if (u.children === l.children && !pe.current) {
              t = qe(e, t, n);
              break e;
            }
          } else
            for (u = t.child, u !== null && (u.return = t); u !== null; ) {
              var o = u.dependencies;
              if (o !== null) {
                i = u.child;
                for (var a = o.firstContext; a !== null; ) {
                  if (a.context === r) {
                    if (u.tag === 1) {
                      (a = Ye(-1, n & -n)), (a.tag = 2);
                      var s = u.updateQueue;
                      if (s !== null) {
                        s = s.shared;
                        var h = s.pending;
                        h === null
                          ? (a.next = a)
                          : ((a.next = h.next), (h.next = a)),
                          (s.pending = a);
                      }
                    }
                    (u.lanes |= n),
                      (a = u.alternate),
                      a !== null && (a.lanes |= n),
                      Vu(u.return, n, t),
                      (o.lanes |= n);
                    break;
                  }
                  a = a.next;
                }
              } else if (u.tag === 10) i = u.type === t.type ? null : u.child;
              else if (u.tag === 18) {
                if (((i = u.return), i === null)) throw Error(k(341));
                (i.lanes |= n),
                  (o = i.alternate),
                  o !== null && (o.lanes |= n),
                  Vu(i, n, t),
                  (i = u.sibling);
              } else i = u.child;
              if (i !== null) i.return = u;
              else
                for (i = u; i !== null; ) {
                  if (i === t) {
                    i = null;
                    break;
                  }
                  if (((u = i.sibling), u !== null)) {
                    (u.return = i.return), (i = u);
                    break;
                  }
                  i = i.return;
                }
              u = i;
            }
        ie(e, t, l.children, n), (t = t.child);
      }
      return t;
    case 9:
      return (
        (l = t.type),
        (r = t.pendingProps.children),
        nn(t, n),
        (l = _e(l)),
        (r = r(l)),
        (t.flags |= 1),
        ie(e, t, r, n),
        t.child
      );
    case 14:
      return (
        (r = t.type),
        (l = Ne(r, t.pendingProps)),
        (l = Ne(r.type, l)),
        ia(e, t, r, l, n)
      );
    case 15:
      return mc(e, t, t.type, t.pendingProps, n);
    case 17:
      return (
        (r = t.type),
        (l = t.pendingProps),
        (l = t.elementType === r ? l : Ne(r, l)),
        Br(e, t),
        (t.tag = 1),
        he(r) ? ((e = !0), rl(t)) : (e = !1),
        nn(t, n),
        dc(t, r, l),
        Qu(t, r, l, n),
        Xu(null, t, r, !0, e, n)
      );
    case 19:
      return Sc(e, t, n);
    case 22:
      return yc(e, t, n);
  }
  throw Error(k(156, t.tag));
};
function Mc(e, t) {
  return ss(e, t);
}
function _p(e, t, n, r) {
  (this.tag = e),
    (this.key = n),
    (this.sibling =
      this.child =
      this.return =
      this.stateNode =
      this.type =
      this.elementType =
        null),
    (this.index = 0),
    (this.ref = null),
    (this.pendingProps = t),
    (this.dependencies =
      this.memoizedState =
      this.updateQueue =
      this.memoizedProps =
        null),
    (this.mode = r),
    (this.subtreeFlags = this.flags = 0),
    (this.deletions = null),
    (this.childLanes = this.lanes = 0),
    (this.alternate = null);
}
function Ce(e, t, n, r) {
  return new _p(e, t, n, r);
}
function qi(e) {
  return (e = e.prototype), !(!e || !e.isReactComponent);
}
function Rp(e) {
  if (typeof e == "function") return qi(e) ? 1 : 0;
  if (e != null) {
    if (((e = e.$$typeof), e === vi)) return 11;
    if (e === gi) return 14;
  }
  return 2;
}
function mt(e, t) {
  var n = e.alternate;
  return (
    n === null
      ? ((n = Ce(e.tag, t, e.key, e.mode)),
        (n.elementType = e.elementType),
        (n.type = e.type),
        (n.stateNode = e.stateNode),
        (n.alternate = e),
        (e.alternate = n))
      : ((n.pendingProps = t),
        (n.type = e.type),
        (n.flags = 0),
        (n.subtreeFlags = 0),
        (n.deletions = null)),
    (n.flags = e.flags & 14680064),
    (n.childLanes = e.childLanes),
    (n.lanes = e.lanes),
    (n.child = e.child),
    (n.memoizedProps = e.memoizedProps),
    (n.memoizedState = e.memoizedState),
    (n.updateQueue = e.updateQueue),
    (t = e.dependencies),
    (n.dependencies =
      t === null ? null : { lanes: t.lanes, firstContext: t.firstContext }),
    (n.sibling = e.sibling),
    (n.index = e.index),
    (n.ref = e.ref),
    n
  );
}
function Wr(e, t, n, r, l, u) {
  var i = 2;
  if (((r = e), typeof e == "function")) qi(e) && (i = 1);
  else if (typeof e == "string") i = 5;
  else
    e: switch (e) {
      case Bt:
        return zt(n.children, l, u, t);
      case yi:
        (i = 8), (l |= 8);
        break;
      case mu:
        return (
          (e = Ce(12, n, t, l | 2)), (e.elementType = mu), (e.lanes = u), e
        );
      case yu:
        return (e = Ce(13, n, t, l)), (e.elementType = yu), (e.lanes = u), e;
      case vu:
        return (e = Ce(19, n, t, l)), (e.elementType = vu), (e.lanes = u), e;
      case Ka:
        return Nl(n, l, u, t);
      default:
        if (typeof e == "object" && e !== null)
          switch (e.$$typeof) {
            case Wa:
              i = 10;
              break e;
            case Qa:
              i = 9;
              break e;
            case vi:
              i = 11;
              break e;
            case gi:
              i = 14;
              break e;
            case nt:
              (i = 16), (r = null);
              break e;
          }
        throw Error(k(130, e == null ? e : typeof e, ""));
    }
  return (
    (t = Ce(i, n, t, l)), (t.elementType = e), (t.type = r), (t.lanes = u), t
  );
}
function zt(e, t, n, r) {
  return (e = Ce(7, e, r, t)), (e.lanes = n), e;
}
function Nl(e, t, n, r) {
  return (
    (e = Ce(22, e, r, t)),
    (e.elementType = Ka),
    (e.lanes = n),
    (e.stateNode = { isHidden: !1 }),
    e
  );
}
function su(e, t, n) {
  return (e = Ce(6, e, null, t)), (e.lanes = n), e;
}
function cu(e, t, n) {
  return (
    (t = Ce(4, e.children !== null ? e.children : [], e.key, t)),
    (t.lanes = n),
    (t.stateNode = {
      containerInfo: e.containerInfo,
      pendingChildren: null,
      implementation: e.implementation,
    }),
    t
  );
}
function Lp(e, t, n, r, l) {
  (this.tag = t),
    (this.containerInfo = e),
    (this.finishedWork =
      this.pingCache =
      this.current =
      this.pendingChildren =
        null),
    (this.timeoutHandle = -1),
    (this.callbackNode = this.pendingContext = this.context = null),
    (this.callbackPriority = 0),
    (this.eventTimes = Wl(0)),
    (this.expirationTimes = Wl(-1)),
    (this.entangledLanes =
      this.finishedLanes =
      this.mutableReadLanes =
      this.expiredLanes =
      this.pingedLanes =
      this.suspendedLanes =
      this.pendingLanes =
        0),
    (this.entanglements = Wl(0)),
    (this.identifierPrefix = r),
    (this.onRecoverableError = l),
    (this.mutableSourceEagerHydrationData = null);
}
function bi(e, t, n, r, l, u, i, o, a) {
  return (
    (e = new Lp(e, t, n, o, a)),
    t === 1 ? ((t = 1), u === !0 && (t |= 8)) : (t = 0),
    (u = Ce(3, null, null, t)),
    (e.current = u),
    (u.stateNode = e),
    (u.memoizedState = {
      element: r,
      isDehydrated: n,
      cache: null,
      transitions: null,
      pendingSuspenseBoundaries: null,
    }),
    Mi(u),
    e
  );
}
function Np(e, t, n) {
  var r = 3 < arguments.length && arguments[3] !== void 0 ? arguments[3] : null;
  return {
    $$typeof: At,
    key: r == null ? null : "" + r,
    children: e,
    containerInfo: t,
    implementation: n,
  };
}
function $c(e) {
  if (!e) return vt;
  e = e._reactInternals;
  e: {
    if (jt(e) !== e || e.tag !== 1) throw Error(k(170));
    var t = e;
    do {
      switch (t.tag) {
        case 3:
          t = t.stateNode.context;
          break e;
        case 1:
          if (he(t.type)) {
            t = t.stateNode.__reactInternalMemoizedMergedChildContext;
            break e;
          }
      }
      t = t.return;
    } while (t !== null);
    throw Error(k(171));
  }
  if (e.tag === 1) {
    var n = e.type;
    if (he(n)) return $s(e, n, t);
  }
  return t;
}
function jc(e, t, n, r, l, u, i, o, a) {
  return (
    (e = bi(n, r, !0, e, l, u, i, o, a)),
    (e.context = $c(null)),
    (n = e.current),
    (r = oe()),
    (l = ht(n)),
    (u = Ye(r, l)),
    (u.callback = t ?? null),
    dt(n, u, l),
    (e.current.lanes = l),
    ir(e, l, r),
    me(e, r),
    e
  );
}
function Tl(e, t, n, r) {
  var l = t.current,
    u = oe(),
    i = ht(l);
  return (
    (n = $c(n)),
    t.context === null ? (t.context = n) : (t.pendingContext = n),
    (t = Ye(u, i)),
    (t.payload = { element: e }),
    (r = r === void 0 ? null : r),
    r !== null && (t.callback = r),
    (e = dt(l, t, i)),
    e !== null && (Ie(e, l, i, u), jr(e, l, i)),
    i
  );
}
function yl(e) {
  if (((e = e.current), !e.child)) return null;
  switch (e.child.tag) {
    case 5:
      return e.child.stateNode;
    default:
      return e.child.stateNode;
  }
}
function ga(e, t) {
  if (((e = e.memoizedState), e !== null && e.dehydrated !== null)) {
    var n = e.retryLane;
    e.retryLane = n !== 0 && n < t ? n : t;
  }
}
function eo(e, t) {
  ga(e, t), (e = e.alternate) && ga(e, t);
}
var Uc =
  typeof reportError == "function"
    ? reportError
    : function (e) {
        console.error(e);
      };
function to(e) {
  this._internalRoot = e;
}
zl.prototype.render = to.prototype.render = function (e) {
  var t = this._internalRoot;
  if (t === null) throw Error(k(409));
  Tl(e, t, null, null);
};
zl.prototype.unmount = to.prototype.unmount = function () {
  var e = this._internalRoot;
  if (e !== null) {
    this._internalRoot = null;
    var t = e.containerInfo;
    Mt(function () {
      Tl(null, e, null, null);
    }),
      (t[Ze] = null);
  }
};
function zl(e) {
  this._internalRoot = e;
}
zl.prototype.unstable_scheduleHydration = function (e) {
  if (e) {
    var t = ys();
    e = { blockedOn: null, target: e, priority: t };
    for (var n = 0; n < lt.length && t !== 0 && t < lt[n].priority; n++);
    lt.splice(n, 0, e), n === 0 && gs(e);
  }
};
function no(e) {
  return !(!e || (e.nodeType !== 1 && e.nodeType !== 9 && e.nodeType !== 11));
}
function Ol(e) {
  return !(
    !e ||
    (e.nodeType !== 1 &&
      e.nodeType !== 9 &&
      e.nodeType !== 11 &&
      (e.nodeType !== 8 || e.nodeValue !== " react-mount-point-unstable "))
  );
}
function wa() {}
function Tp(e, t, n, r, l) {
  if (l) {
    if (typeof r == "function") {
      var u = r;
      r = function () {
        var s = yl(i);
        u.call(s);
      };
    }
    var i = jc(t, r, e, 0, null, !1, !1, "", wa);
    return (
      (e._reactRootContainer = i),
      (e[Ze] = i.current),
      Gn(e.nodeType === 8 ? e.parentNode : e),
      Mt(),
      i
    );
  }
  for (; (l = e.lastChild); ) e.removeChild(l);
  if (typeof r == "function") {
    var o = r;
    r = function () {
      var s = yl(a);
      o.call(s);
    };
  }
  var a = bi(e, 0, !1, null, null, !1, !1, "", wa);
  return (
    (e._reactRootContainer = a),
    (e[Ze] = a.current),
    Gn(e.nodeType === 8 ? e.parentNode : e),
    Mt(function () {
      Tl(t, a, n, r);
    }),
    a
  );
}
function Il(e, t, n, r, l) {
  var u = n._reactRootContainer;
  if (u) {
    var i = u;
    if (typeof l == "function") {
      var o = l;
      l = function () {
        var a = yl(i);
        o.call(a);
      };
    }
    Tl(t, i, e, l);
  } else i = Tp(n, t, e, l, r);
  return yl(i);
}
hs = function (e) {
  switch (e.tag) {
    case 3:
      var t = e.stateNode;
      if (t.current.memoizedState.isDehydrated) {
        var n = Tn(t.pendingLanes);
        n !== 0 &&
          (ki(t, n | 1), me(t, K()), !(D & 6) && ((fn = K() + 500), kt()));
      }
      break;
    case 13:
      Mt(function () {
        var r = Je(e, 1);
        if (r !== null) {
          var l = oe();
          Ie(r, e, 1, l);
        }
      }),
        eo(e, 1);
  }
};
Ei = function (e) {
  if (e.tag === 13) {
    var t = Je(e, 134217728);
    if (t !== null) {
      var n = oe();
      Ie(t, e, 134217728, n);
    }
    eo(e, 134217728);
  }
};
ms = function (e) {
  if (e.tag === 13) {
    var t = ht(e),
      n = Je(e, t);
    if (n !== null) {
      var r = oe();
      Ie(n, e, t, r);
    }
    eo(e, t);
  }
};
ys = function () {
  return F;
};
vs = function (e, t) {
  var n = F;
  try {
    return (F = e), t();
  } finally {
    F = n;
  }
};
Ru = function (e, t, n) {
  switch (t) {
    case "input":
      if ((Su(e, n), (t = n.name), n.type === "radio" && t != null)) {
        for (n = e; n.parentNode; ) n = n.parentNode;
        for (
          n = n.querySelectorAll(
            "input[name=" + JSON.stringify("" + t) + '][type="radio"]'
          ),
            t = 0;
          t < n.length;
          t++
        ) {
          var r = n[t];
          if (r !== e && r.form === e.form) {
            var l = xl(r);
            if (!l) throw Error(k(90));
            Xa(r), Su(r, l);
          }
        }
      }
      break;
    case "textarea":
      Za(e, n);
      break;
    case "select":
      (t = n.value), t != null && qt(e, !!n.multiple, t, !1);
  }
};
rs = Gi;
ls = Mt;
var zp = { usingClientEntryPoint: !1, Events: [ar, Qt, xl, ts, ns, Gi] },
  _n = {
    findFiberByHostInstance: Rt,
    bundleType: 0,
    version: "18.3.1",
    rendererPackageName: "react-dom",
  },
  Op = {
    bundleType: _n.bundleType,
    version: _n.version,
    rendererPackageName: _n.rendererPackageName,
    rendererConfig: _n.rendererConfig,
    overrideHookState: null,
    overrideHookStateDeletePath: null,
    overrideHookStateRenamePath: null,
    overrideProps: null,
    overridePropsDeletePath: null,
    overridePropsRenamePath: null,
    setErrorHandler: null,
    setSuspenseHandler: null,
    scheduleUpdate: null,
    currentDispatcherRef: be.ReactCurrentDispatcher,
    findHostInstanceByFiber: function (e) {
      return (e = os(e)), e === null ? null : e.stateNode;
    },
    findFiberByHostInstance: _n.findFiberByHostInstance,
    findHostInstancesForRefresh: null,
    scheduleRefresh: null,
    scheduleRoot: null,
    setRefreshHandler: null,
    getCurrentFiber: null,
    reconcilerVersion: "18.3.1-next-f1338f8080-20240426",
  };
if (typeof __REACT_DEVTOOLS_GLOBAL_HOOK__ < "u") {
  var zr = __REACT_DEVTOOLS_GLOBAL_HOOK__;
  if (!zr.isDisabled && zr.supportsFiber)
    try {
      (wl = zr.inject(Op)), (Ue = zr);
    } catch {}
}
Se.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED = zp;
Se.createPortal = function (e, t) {
  var n = 2 < arguments.length && arguments[2] !== void 0 ? arguments[2] : null;
  if (!no(t)) throw Error(k(200));
  return Np(e, t, null, n);
};
Se.createRoot = function (e, t) {
  if (!no(e)) throw Error(k(299));
  var n = !1,
    r = "",
    l = Uc;
  return (
    t != null &&
      (t.unstable_strictMode === !0 && (n = !0),
      t.identifierPrefix !== void 0 && (r = t.identifierPrefix),
      t.onRecoverableError !== void 0 && (l = t.onRecoverableError)),
    (t = bi(e, 1, !1, null, null, n, !1, r, l)),
    (e[Ze] = t.current),
    Gn(e.nodeType === 8 ? e.parentNode : e),
    new to(t)
  );
};
Se.findDOMNode = function (e) {
  if (e == null) return null;
  if (e.nodeType === 1) return e;
  var t = e._reactInternals;
  if (t === void 0)
    throw typeof e.render == "function"
      ? Error(k(188))
      : ((e = Object.keys(e).join(",")), Error(k(268, e)));
  return (e = os(t)), (e = e === null ? null : e.stateNode), e;
};
Se.flushSync = function (e) {
  return Mt(e);
};
Se.hydrate = function (e, t, n) {
  if (!Ol(t)) throw Error(k(200));
  return Il(null, e, t, !0, n);
};
Se.hydrateRoot = function (e, t, n) {
  if (!no(e)) throw Error(k(405));
  var r = (n != null && n.hydratedSources) || null,
    l = !1,
    u = "",
    i = Uc;
  if (
    (n != null &&
      (n.unstable_strictMode === !0 && (l = !0),
      n.identifierPrefix !== void 0 && (u = n.identifierPrefix),
      n.onRecoverableError !== void 0 && (i = n.onRecoverableError)),
    (t = jc(t, null, e, 1, n ?? null, l, !1, u, i)),
    (e[Ze] = t.current),
    Gn(e),
    r)
  )
    for (e = 0; e < r.length; e++)
      (n = r[e]),
        (l = n._getVersion),
        (l = l(n._source)),
        t.mutableSourceEagerHydrationData == null
          ? (t.mutableSourceEagerHydrationData = [n, l])
          : t.mutableSourceEagerHydrationData.push(n, l);
  return new zl(t);
};
Se.render = function (e, t, n) {
  if (!Ol(t)) throw Error(k(200));
  return Il(null, e, t, !1, n);
};
Se.unmountComponentAtNode = function (e) {
  if (!Ol(e)) throw Error(k(40));
  return e._reactRootContainer
    ? (Mt(function () {
        Il(null, null, e, !1, function () {
          (e._reactRootContainer = null), (e[Ze] = null);
        });
      }),
      !0)
    : !1;
};
Se.unstable_batchedUpdates = Gi;
Se.unstable_renderSubtreeIntoContainer = function (e, t, n, r) {
  if (!Ol(n)) throw Error(k(200));
  if (e == null || e._reactInternals === void 0) throw Error(k(38));
  return Il(e, t, n, !1, r);
};
Se.version = "18.3.1-next-f1338f8080-20240426";
function Ac() {
  if (
    !(
      typeof __REACT_DEVTOOLS_GLOBAL_HOOK__ > "u" ||
      typeof __REACT_DEVTOOLS_GLOBAL_HOOK__.checkDCE != "function"
    )
  )
    try {
      __REACT_DEVTOOLS_GLOBAL_HOOK__.checkDCE(Ac);
    } catch (e) {
      console.error(e);
    }
}
Ac(), (Aa.exports = Se);
var Ip = Aa.exports,
  Sa = Ip;
(pu.createRoot = Sa.createRoot), (pu.hydrateRoot = Sa.hydrateRoot);
var ro = {};
Object.defineProperty(ro, "__esModule", { value: !0 });
ro.parse = Ap;
ro.serialize = Bp;
const Dp = /^[\u0021-\u003A\u003C\u003E-\u007E]+$/,
  Fp = /^[\u0021-\u003A\u003C-\u007E]*$/,
  Mp =
    /^([.]?[a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?)([.][a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?)*$/i,
  $p = /^[\u0020-\u003A\u003D-\u007E]*$/,
  jp = Object.prototype.toString,
  Up = (() => {
    const e = function () {};
    return (e.prototype = Object.create(null)), e;
  })();
function Ap(e, t) {
  const n = new Up(),
    r = e.length;
  if (r < 2) return n;
  const l = (t == null ? void 0 : t.decode) || Hp;
  let u = 0;
  do {
    const i = e.indexOf("=", u);
    if (i === -1) break;
    const o = e.indexOf(";", u),
      a = o === -1 ? r : o;
    if (i > a) {
      u = e.lastIndexOf(";", i - 1) + 1;
      continue;
    }
    const s = ka(e, u, i),
      h = Ea(e, i, s),
      m = e.slice(s, h);
    if (n[m] === void 0) {
      let p = ka(e, i + 1, a),
        y = Ea(e, a, p);
      const S = l(e.slice(p, y));
      n[m] = S;
    }
    u = a + 1;
  } while (u < r);
  return n;
}
function ka(e, t, n) {
  do {
    const r = e.charCodeAt(t);
    if (r !== 32 && r !== 9) return t;
  } while (++t < n);
  return n;
}
function Ea(e, t, n) {
  for (; t > n; ) {
    const r = e.charCodeAt(--t);
    if (r !== 32 && r !== 9) return t + 1;
  }
  return n;
}
function Bp(e, t, n) {
  const r = (n == null ? void 0 : n.encode) || encodeURIComponent;
  if (!Dp.test(e)) throw new TypeError(`argument name is invalid: ${e}`);
  const l = r(t);
  if (!Fp.test(l)) throw new TypeError(`argument val is invalid: ${t}`);
  let u = e + "=" + l;
  if (!n) return u;
  if (n.maxAge !== void 0) {
    if (!Number.isInteger(n.maxAge))
      throw new TypeError(`option maxAge is invalid: ${n.maxAge}`);
    u += "; Max-Age=" + n.maxAge;
  }
  if (n.domain) {
    if (!Mp.test(n.domain))
      throw new TypeError(`option domain is invalid: ${n.domain}`);
    u += "; Domain=" + n.domain;
  }
  if (n.path) {
    if (!$p.test(n.path))
      throw new TypeError(`option path is invalid: ${n.path}`);
    u += "; Path=" + n.path;
  }
  if (n.expires) {
    if (!Vp(n.expires) || !Number.isFinite(n.expires.valueOf()))
      throw new TypeError(`option expires is invalid: ${n.expires}`);
    u += "; Expires=" + n.expires.toUTCString();
  }
  if (
    (n.httpOnly && (u += "; HttpOnly"),
    n.secure && (u += "; Secure"),
    n.partitioned && (u += "; Partitioned"),
    n.priority)
  )
    switch (typeof n.priority == "string" ? n.priority.toLowerCase() : void 0) {
      case "low":
        u += "; Priority=Low";
        break;
      case "medium":
        u += "; Priority=Medium";
        break;
      case "high":
        u += "; Priority=High";
        break;
      default:
        throw new TypeError(`option priority is invalid: ${n.priority}`);
    }
  if (n.sameSite)
    switch (
      typeof n.sameSite == "string" ? n.sameSite.toLowerCase() : n.sameSite
    ) {
      case !0:
      case "strict":
        u += "; SameSite=Strict";
        break;
      case "lax":
        u += "; SameSite=Lax";
        break;
      case "none":
        u += "; SameSite=None";
        break;
      default:
        throw new TypeError(`option sameSite is invalid: ${n.sameSite}`);
    }
  return u;
}
function Hp(e) {
  if (e.indexOf("%") === -1) return e;
  try {
    return decodeURIComponent(e);
  } catch {
    return e;
  }
}
function Vp(e) {
  return jp.call(e) === "[object Date]";
}
/**
 * react-router v7.1.3
 *
 * Copyright (c) Remix Software Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE.md file in the root directory of this source tree.
 *
 * @license MIT
 */ var xa = "popstate";
function Wp(e = {}) {
  function t(r, l) {
    let { pathname: u, search: i, hash: o } = r.location;
    return ii(
      "",
      { pathname: u, search: i, hash: o },
      (l.state && l.state.usr) || null,
      (l.state && l.state.key) || "default"
    );
  }
  function n(r, l) {
    return typeof l == "string" ? l : lr(l);
  }
  return Kp(t, n, null, e);
}
function H(e, t) {
  if (e === !1 || e === null || typeof e > "u") throw new Error(t);
}
function Fe(e, t) {
  if (!e) {
    typeof console < "u" && console.warn(t);
    try {
      throw new Error(t);
    } catch {}
  }
}
function Qp() {
  return Math.random().toString(36).substring(2, 10);
}
function Ca(e, t) {
  return { usr: e.state, key: e.key, idx: t };
}
function ii(e, t, n = null, r) {
  return {
    pathname: typeof e == "string" ? e : e.pathname,
    search: "",
    hash: "",
    ...(typeof t == "string" ? mn(t) : t),
    state: n,
    key: (t && t.key) || r || Qp(),
  };
}
function lr({ pathname: e = "/", search: t = "", hash: n = "" }) {
  return (
    t && t !== "?" && (e += t.charAt(0) === "?" ? t : "?" + t),
    n && n !== "#" && (e += n.charAt(0) === "#" ? n : "#" + n),
    e
  );
}
function mn(e) {
  let t = {};
  if (e) {
    let n = e.indexOf("#");
    n >= 0 && ((t.hash = e.substring(n)), (e = e.substring(0, n)));
    let r = e.indexOf("?");
    r >= 0 && ((t.search = e.substring(r)), (e = e.substring(0, r))),
      e && (t.pathname = e);
  }
  return t;
}
function Kp(e, t, n, r = {}) {
  let { window: l = document.defaultView, v5Compat: u = !1 } = r,
    i = l.history,
    o = "POP",
    a = null,
    s = h();
  s == null && ((s = 0), i.replaceState({ ...i.state, idx: s }, ""));
  function h() {
    return (i.state || { idx: null }).idx;
  }
  function m() {
    o = "POP";
    let x = h(),
      f = x == null ? null : x - s;
    (s = x), a && a({ action: o, location: g.location, delta: f });
  }
  function p(x, f) {
    o = "PUSH";
    let c = ii(g.location, x, f);
    s = h() + 1;
    let d = Ca(c, s),
      w = g.createHref(c);
    try {
      i.pushState(d, "", w);
    } catch (E) {
      if (E instanceof DOMException && E.name === "DataCloneError") throw E;
      l.location.assign(w);
    }
    u && a && a({ action: o, location: g.location, delta: 1 });
  }
  function y(x, f) {
    o = "REPLACE";
    let c = ii(g.location, x, f);
    s = h();
    let d = Ca(c, s),
      w = g.createHref(c);
    i.replaceState(d, "", w),
      u && a && a({ action: o, location: g.location, delta: 0 });
  }
  function S(x) {
    let f = l.location.origin !== "null" ? l.location.origin : l.location.href,
      c = typeof x == "string" ? x : lr(x);
    return (
      (c = c.replace(/ $/, "%20")),
      H(
        f,
        `No window.location.(origin|href) available to create URL for href: ${c}`
      ),
      new URL(c, f)
    );
  }
  let g = {
    get action() {
      return o;
    },
    get location() {
      return e(l, i);
    },
    listen(x) {
      if (a) throw new Error("A history only accepts one active listener");
      return (
        l.addEventListener(xa, m),
        (a = x),
        () => {
          l.removeEventListener(xa, m), (a = null);
        }
      );
    },
    createHref(x) {
      return t(l, x);
    },
    createURL: S,
    encodeLocation(x) {
      let f = S(x);
      return { pathname: f.pathname, search: f.search, hash: f.hash };
    },
    push: p,
    replace: y,
    go(x) {
      return i.go(x);
    },
  };
  return g;
}
function Bc(e, t, n = "/") {
  return Yp(e, t, n, !1);
}
function Yp(e, t, n, r) {
  let l = typeof t == "string" ? mn(t) : t,
    u = gt(l.pathname || "/", n);
  if (u == null) return null;
  let i = Hc(e);
  Xp(i);
  let o = null;
  for (let a = 0; o == null && a < i.length; ++a) {
    let s = uh(u);
    o = rh(i[a], s, r);
  }
  return o;
}
function Hc(e, t = [], n = [], r = "") {
  let l = (u, i, o) => {
    let a = {
      relativePath: o === void 0 ? u.path || "" : o,
      caseSensitive: u.caseSensitive === !0,
      childrenIndex: i,
      route: u,
    };
    a.relativePath.startsWith("/") &&
      (H(
        a.relativePath.startsWith(r),
        `Absolute route path "${a.relativePath}" nested under path "${r}" is not valid. An absolute child route path must start with the combined path of all its parent routes.`
      ),
      (a.relativePath = a.relativePath.slice(r.length)));
    let s = Xe([r, a.relativePath]),
      h = n.concat(a);
    u.children &&
      u.children.length > 0 &&
      (H(
        u.index !== !0,
        `Index routes must not have child routes. Please remove all child routes from route path "${s}".`
      ),
      Hc(u.children, t, h, s)),
      !(u.path == null && !u.index) &&
        t.push({ path: s, score: th(s, u.index), routesMeta: h });
  };
  return (
    e.forEach((u, i) => {
      var o;
      if (u.path === "" || !((o = u.path) != null && o.includes("?"))) l(u, i);
      else for (let a of Vc(u.path)) l(u, i, a);
    }),
    t
  );
}
function Vc(e) {
  let t = e.split("/");
  if (t.length === 0) return [];
  let [n, ...r] = t,
    l = n.endsWith("?"),
    u = n.replace(/\?$/, "");
  if (r.length === 0) return l ? [u, ""] : [u];
  let i = Vc(r.join("/")),
    o = [];
  return (
    o.push(...i.map((a) => (a === "" ? u : [u, a].join("/")))),
    l && o.push(...i),
    o.map((a) => (e.startsWith("/") && a === "" ? "/" : a))
  );
}
function Xp(e) {
  e.sort((t, n) =>
    t.score !== n.score
      ? n.score - t.score
      : nh(
          t.routesMeta.map((r) => r.childrenIndex),
          n.routesMeta.map((r) => r.childrenIndex)
        )
  );
}
var Gp = /^:[\w-]+$/,
  Zp = 3,
  Jp = 2,
  qp = 1,
  bp = 10,
  eh = -2,
  Pa = (e) => e === "*";
function th(e, t) {
  let n = e.split("/"),
    r = n.length;
  return (
    n.some(Pa) && (r += eh),
    t && (r += Jp),
    n
      .filter((l) => !Pa(l))
      .reduce((l, u) => l + (Gp.test(u) ? Zp : u === "" ? qp : bp), r)
  );
}
function nh(e, t) {
  return e.length === t.length && e.slice(0, -1).every((r, l) => r === t[l])
    ? e[e.length - 1] - t[t.length - 1]
    : 0;
}
function rh(e, t, n = !1) {
  let { routesMeta: r } = e,
    l = {},
    u = "/",
    i = [];
  for (let o = 0; o < r.length; ++o) {
    let a = r[o],
      s = o === r.length - 1,
      h = u === "/" ? t : t.slice(u.length) || "/",
      m = vl(
        { path: a.relativePath, caseSensitive: a.caseSensitive, end: s },
        h
      ),
      p = a.route;
    if (
      (!m &&
        s &&
        n &&
        !r[r.length - 1].route.index &&
        (m = vl(
          { path: a.relativePath, caseSensitive: a.caseSensitive, end: !1 },
          h
        )),
      !m)
    )
      return null;
    Object.assign(l, m.params),
      i.push({
        params: l,
        pathname: Xe([u, m.pathname]),
        pathnameBase: sh(Xe([u, m.pathnameBase])),
        route: p,
      }),
      m.pathnameBase !== "/" && (u = Xe([u, m.pathnameBase]));
  }
  return i;
}
function vl(e, t) {
  typeof e == "string" && (e = { path: e, caseSensitive: !1, end: !0 });
  let [n, r] = lh(e.path, e.caseSensitive, e.end),
    l = t.match(n);
  if (!l) return null;
  let u = l[0],
    i = u.replace(/(.)\/+$/, "$1"),
    o = l.slice(1);
  return {
    params: r.reduce((s, { paramName: h, isOptional: m }, p) => {
      if (h === "*") {
        let S = o[p] || "";
        i = u.slice(0, u.length - S.length).replace(/(.)\/+$/, "$1");
      }
      const y = o[p];
      return (
        m && !y ? (s[h] = void 0) : (s[h] = (y || "").replace(/%2F/g, "/")), s
      );
    }, {}),
    pathname: u,
    pathnameBase: i,
    pattern: e,
  };
}
function lh(e, t = !1, n = !0) {
  Fe(
    e === "*" || !e.endsWith("*") || e.endsWith("/*"),
    `Route path "${e}" will be treated as if it were "${e.replace(
      /\*$/,
      "/*"
    )}" because the \`*\` character must always follow a \`/\` in the pattern. To get rid of this warning, please change the route path to "${e.replace(
      /\*$/,
      "/*"
    )}".`
  );
  let r = [],
    l =
      "^" +
      e
        .replace(/\/*\*?$/, "")
        .replace(/^\/*/, "/")
        .replace(/[\\.*+^${}|()[\]]/g, "\\$&")
        .replace(
          /\/:([\w-]+)(\?)?/g,
          (i, o, a) => (
            r.push({ paramName: o, isOptional: a != null }),
            a ? "/?([^\\/]+)?" : "/([^\\/]+)"
          )
        );
  return (
    e.endsWith("*")
      ? (r.push({ paramName: "*" }),
        (l += e === "*" || e === "/*" ? "(.*)$" : "(?:\\/(.+)|\\/*)$"))
      : n
      ? (l += "\\/*$")
      : e !== "" && e !== "/" && (l += "(?:(?=\\/|$))"),
    [new RegExp(l, t ? void 0 : "i"), r]
  );
}
function uh(e) {
  try {
    return e
      .split("/")
      .map((t) => decodeURIComponent(t).replace(/\//g, "%2F"))
      .join("/");
  } catch (t) {
    return (
      Fe(
        !1,
        `The URL path "${e}" could not be decoded because it is a malformed URL segment. This is probably due to a bad percent encoding (${t}).`
      ),
      e
    );
  }
}
function gt(e, t) {
  if (t === "/") return e;
  if (!e.toLowerCase().startsWith(t.toLowerCase())) return null;
  let n = t.endsWith("/") ? t.length - 1 : t.length,
    r = e.charAt(n);
  return r && r !== "/" ? null : e.slice(n) || "/";
}
function ih(e, t = "/") {
  let {
    pathname: n,
    search: r = "",
    hash: l = "",
  } = typeof e == "string" ? mn(e) : e;
  return {
    pathname: n ? (n.startsWith("/") ? n : oh(n, t)) : t,
    search: ch(r),
    hash: fh(l),
  };
}
function oh(e, t) {
  let n = t.replace(/\/+$/, "").split("/");
  return (
    e.split("/").forEach((l) => {
      l === ".." ? n.length > 1 && n.pop() : l !== "." && n.push(l);
    }),
    n.length > 1 ? n.join("/") : "/"
  );
}
function fu(e, t, n, r) {
  return `Cannot include a '${e}' character in a manually specified \`to.${t}\` field [${JSON.stringify(
    r
  )}].  Please separate it out to the \`to.${n}\` field. Alternatively you may provide the full path as a string in <Link to="..."> and the router will parse it for you.`;
}
function ah(e) {
  return e.filter(
    (t, n) => n === 0 || (t.route.path && t.route.path.length > 0)
  );
}
function Wc(e) {
  let t = ah(e);
  return t.map((n, r) => (r === t.length - 1 ? n.pathname : n.pathnameBase));
}
function Qc(e, t, n, r = !1) {
  let l;
  typeof e == "string"
    ? (l = mn(e))
    : ((l = { ...e }),
      H(
        !l.pathname || !l.pathname.includes("?"),
        fu("?", "pathname", "search", l)
      ),
      H(
        !l.pathname || !l.pathname.includes("#"),
        fu("#", "pathname", "hash", l)
      ),
      H(!l.search || !l.search.includes("#"), fu("#", "search", "hash", l)));
  let u = e === "" || l.pathname === "",
    i = u ? "/" : l.pathname,
    o;
  if (i == null) o = n;
  else {
    let m = t.length - 1;
    if (!r && i.startsWith("..")) {
      let p = i.split("/");
      for (; p[0] === ".."; ) p.shift(), (m -= 1);
      l.pathname = p.join("/");
    }
    o = m >= 0 ? t[m] : "/";
  }
  let a = ih(l, o),
    s = i && i !== "/" && i.endsWith("/"),
    h = (u || i === ".") && n.endsWith("/");
  return !a.pathname.endsWith("/") && (s || h) && (a.pathname += "/"), a;
}
var Xe = (e) => e.join("/").replace(/\/\/+/g, "/"),
  sh = (e) => e.replace(/\/+$/, "").replace(/^\/*/, "/"),
  ch = (e) => (!e || e === "?" ? "" : e.startsWith("?") ? e : "?" + e),
  fh = (e) => (!e || e === "#" ? "" : e.startsWith("#") ? e : "#" + e);
function dh(e) {
  return (
    e != null &&
    typeof e.status == "number" &&
    typeof e.statusText == "string" &&
    typeof e.internal == "boolean" &&
    "data" in e
  );
}
var Kc = ["POST", "PUT", "PATCH", "DELETE"];
new Set(Kc);
var ph = ["GET", ...Kc];
new Set(ph);
var yn = v.createContext(null);
yn.displayName = "DataRouter";
var Dl = v.createContext(null);
Dl.displayName = "DataRouterState";
var Yc = v.createContext({ isTransitioning: !1 });
Yc.displayName = "ViewTransition";
var hh = v.createContext(new Map());
hh.displayName = "Fetchers";
var mh = v.createContext(null);
mh.displayName = "Await";
var Be = v.createContext(null);
Be.displayName = "Navigation";
var cr = v.createContext(null);
cr.displayName = "Location";
var et = v.createContext({ outlet: null, matches: [], isDataRoute: !1 });
et.displayName = "Route";
var lo = v.createContext(null);
lo.displayName = "RouteError";
function yh(e, { relative: t } = {}) {
  H(fr(), "useHref() may be used only in the context of a <Router> component.");
  let { basename: n, navigator: r } = v.useContext(Be),
    { hash: l, pathname: u, search: i } = dr(e, { relative: t }),
    o = u;
  return (
    n !== "/" && (o = u === "/" ? n : Xe([n, u])),
    r.createHref({ pathname: o, search: i, hash: l })
  );
}
function fr() {
  return v.useContext(cr) != null;
}
function Et() {
  return (
    H(
      fr(),
      "useLocation() may be used only in the context of a <Router> component."
    ),
    v.useContext(cr).location
  );
}
var Xc =
  "You should call navigate() in a React.useEffect(), not when your component is first rendered.";
function Gc(e) {
  v.useContext(Be).static || v.useLayoutEffect(e);
}
function Zc() {
  let { isDataRoute: e } = v.useContext(et);
  return e ? Nh() : vh();
}
function vh() {
  H(
    fr(),
    "useNavigate() may be used only in the context of a <Router> component."
  );
  let e = v.useContext(yn),
    { basename: t, navigator: n } = v.useContext(Be),
    { matches: r } = v.useContext(et),
    { pathname: l } = Et(),
    u = JSON.stringify(Wc(r)),
    i = v.useRef(!1);
  return (
    Gc(() => {
      i.current = !0;
    }),
    v.useCallback(
      (a, s = {}) => {
        if ((Fe(i.current, Xc), !i.current)) return;
        if (typeof a == "number") {
          n.go(a);
          return;
        }
        let h = Qc(a, JSON.parse(u), l, s.relative === "path");
        e == null &&
          t !== "/" &&
          (h.pathname = h.pathname === "/" ? t : Xe([t, h.pathname])),
          (s.replace ? n.replace : n.push)(h, s.state, s);
      },
      [t, n, u, l, e]
    )
  );
}
v.createContext(null);
function dr(e, { relative: t } = {}) {
  let { matches: n } = v.useContext(et),
    { pathname: r } = Et(),
    l = JSON.stringify(Wc(n));
  return v.useMemo(() => Qc(e, JSON.parse(l), r, t === "path"), [e, l, r, t]);
}
function gh(e, t) {
  return Jc(e, t);
}
function Jc(e, t, n, r) {
  var f;
  H(
    fr(),
    "useRoutes() may be used only in the context of a <Router> component."
  );
  let { navigator: l } = v.useContext(Be),
    { matches: u } = v.useContext(et),
    i = u[u.length - 1],
    o = i ? i.params : {},
    a = i ? i.pathname : "/",
    s = i ? i.pathnameBase : "/",
    h = i && i.route;
  {
    let c = (h && h.path) || "";
    qc(
      a,
      !h || c.endsWith("*") || c.endsWith("*?"),
      `You rendered descendant <Routes> (or called \`useRoutes()\`) at "${a}" (under <Route path="${c}">) but the parent route path has no trailing "*". This means if you navigate deeper, the parent won't match anymore and therefore the child routes will never render.

Please change the parent <Route path="${c}"> to <Route path="${
        c === "/" ? "*" : `${c}/*`
      }">.`
    );
  }
  let m = Et(),
    p;
  if (t) {
    let c = typeof t == "string" ? mn(t) : t;
    H(
      s === "/" || ((f = c.pathname) == null ? void 0 : f.startsWith(s)),
      `When overriding the location using \`<Routes location>\` or \`useRoutes(routes, location)\`, the location pathname must begin with the portion of the URL pathname that was matched by all parent routes. The current pathname base is "${s}" but pathname "${c.pathname}" was given in the \`location\` prop.`
    ),
      (p = c);
  } else p = m;
  let y = p.pathname || "/",
    S = y;
  if (s !== "/") {
    let c = s.replace(/^\//, "").split("/");
    S = "/" + y.replace(/^\//, "").split("/").slice(c.length).join("/");
  }
  let g = Bc(e, { pathname: S });
  Fe(
    h || g != null,
    `No routes matched location "${p.pathname}${p.search}${p.hash}" `
  ),
    Fe(
      g == null ||
        g[g.length - 1].route.element !== void 0 ||
        g[g.length - 1].route.Component !== void 0 ||
        g[g.length - 1].route.lazy !== void 0,
      `Matched leaf route at location "${p.pathname}${p.search}${p.hash}" does not have an element or Component. This means it will render an <Outlet /> with a null value by default resulting in an "empty" page.`
    );
  let x = xh(
    g &&
      g.map((c) =>
        Object.assign({}, c, {
          params: Object.assign({}, o, c.params),
          pathname: Xe([
            s,
            l.encodeLocation
              ? l.encodeLocation(c.pathname).pathname
              : c.pathname,
          ]),
          pathnameBase:
            c.pathnameBase === "/"
              ? s
              : Xe([
                  s,
                  l.encodeLocation
                    ? l.encodeLocation(c.pathnameBase).pathname
                    : c.pathnameBase,
                ]),
        })
      ),
    u,
    n,
    r
  );
  return t && x
    ? v.createElement(
        cr.Provider,
        {
          value: {
            location: {
              pathname: "/",
              search: "",
              hash: "",
              state: null,
              key: "default",
              ...p,
            },
            navigationType: "POP",
          },
        },
        x
      )
    : x;
}
function wh() {
  let e = Lh(),
    t = dh(e)
      ? `${e.status} ${e.statusText}`
      : e instanceof Error
      ? e.message
      : JSON.stringify(e),
    n = e instanceof Error ? e.stack : null,
    r = "rgba(200,200,200, 0.5)",
    l = { padding: "0.5rem", backgroundColor: r },
    u = { padding: "2px 4px", backgroundColor: r },
    i = null;
  return (
    console.error("Error handled by React Router default ErrorBoundary:", e),
    (i = v.createElement(
      v.Fragment,
      null,
      v.createElement("p", null, "💿 Hey developer 👋"),
      v.createElement(
        "p",
        null,
        "You can provide a way better UX than this when your app throws errors by providing your own ",
        v.createElement("code", { style: u }, "ErrorBoundary"),
        " or",
        " ",
        v.createElement("code", { style: u }, "errorElement"),
        " prop on your route."
      )
    )),
    v.createElement(
      v.Fragment,
      null,
      v.createElement("h2", null, "Unexpected Application Error!"),
      v.createElement("h3", { style: { fontStyle: "italic" } }, t),
      n ? v.createElement("pre", { style: l }, n) : null,
      i
    )
  );
}
var Sh = v.createElement(wh, null),
  kh = class extends v.Component {
    constructor(e) {
      super(e),
        (this.state = {
          location: e.location,
          revalidation: e.revalidation,
          error: e.error,
        });
    }
    static getDerivedStateFromError(e) {
      return { error: e };
    }
    static getDerivedStateFromProps(e, t) {
      return t.location !== e.location ||
        (t.revalidation !== "idle" && e.revalidation === "idle")
        ? { error: e.error, location: e.location, revalidation: e.revalidation }
        : {
            error: e.error !== void 0 ? e.error : t.error,
            location: t.location,
            revalidation: e.revalidation || t.revalidation,
          };
    }
    componentDidCatch(e, t) {
      console.error(
        "React Router caught the following error during render",
        e,
        t
      );
    }
    render() {
      return this.state.error !== void 0
        ? v.createElement(
            et.Provider,
            { value: this.props.routeContext },
            v.createElement(lo.Provider, {
              value: this.state.error,
              children: this.props.component,
            })
          )
        : this.props.children;
    }
  };
function Eh({ routeContext: e, match: t, children: n }) {
  let r = v.useContext(yn);
  return (
    r &&
      r.static &&
      r.staticContext &&
      (t.route.errorElement || t.route.ErrorBoundary) &&
      (r.staticContext._deepestRenderedBoundaryId = t.route.id),
    v.createElement(et.Provider, { value: e }, n)
  );
}
function xh(e, t = [], n = null, r = null) {
  if (e == null) {
    if (!n) return null;
    if (n.errors) e = n.matches;
    else if (t.length === 0 && !n.initialized && n.matches.length > 0)
      e = n.matches;
    else return null;
  }
  let l = e,
    u = n == null ? void 0 : n.errors;
  if (u != null) {
    let a = l.findIndex(
      (s) => s.route.id && (u == null ? void 0 : u[s.route.id]) !== void 0
    );
    H(
      a >= 0,
      `Could not find a matching route for errors on route IDs: ${Object.keys(
        u
      ).join(",")}`
    ),
      (l = l.slice(0, Math.min(l.length, a + 1)));
  }
  let i = !1,
    o = -1;
  if (n)
    for (let a = 0; a < l.length; a++) {
      let s = l[a];
      if (
        ((s.route.HydrateFallback || s.route.hydrateFallbackElement) && (o = a),
        s.route.id)
      ) {
        let { loaderData: h, errors: m } = n,
          p =
            s.route.loader &&
            !h.hasOwnProperty(s.route.id) &&
            (!m || m[s.route.id] === void 0);
        if (s.route.lazy || p) {
          (i = !0), o >= 0 ? (l = l.slice(0, o + 1)) : (l = [l[0]]);
          break;
        }
      }
    }
  return l.reduceRight((a, s, h) => {
    let m,
      p = !1,
      y = null,
      S = null;
    n &&
      ((m = u && s.route.id ? u[s.route.id] : void 0),
      (y = s.route.errorElement || Sh),
      i &&
        (o < 0 && h === 0
          ? (qc(
              "route-fallback",
              !1,
              "No `HydrateFallback` element provided to render during initial hydration"
            ),
            (p = !0),
            (S = null))
          : o === h &&
            ((p = !0), (S = s.route.hydrateFallbackElement || null))));
    let g = t.concat(l.slice(0, h + 1)),
      x = () => {
        let f;
        return (
          m
            ? (f = y)
            : p
            ? (f = S)
            : s.route.Component
            ? (f = v.createElement(s.route.Component, null))
            : s.route.element
            ? (f = s.route.element)
            : (f = a),
          v.createElement(Eh, {
            match: s,
            routeContext: { outlet: a, matches: g, isDataRoute: n != null },
            children: f,
          })
        );
      };
    return n && (s.route.ErrorBoundary || s.route.errorElement || h === 0)
      ? v.createElement(kh, {
          location: n.location,
          revalidation: n.revalidation,
          component: y,
          error: m,
          children: x(),
          routeContext: { outlet: null, matches: g, isDataRoute: !0 },
        })
      : x();
  }, null);
}
function uo(e) {
  return `${e} must be used within a data router.  See https://reactrouter.com/en/main/routers/picking-a-router.`;
}
function Ch(e) {
  let t = v.useContext(yn);
  return H(t, uo(e)), t;
}
function Ph(e) {
  let t = v.useContext(Dl);
  return H(t, uo(e)), t;
}
function _h(e) {
  let t = v.useContext(et);
  return H(t, uo(e)), t;
}
function io(e) {
  let t = _h(e),
    n = t.matches[t.matches.length - 1];
  return (
    H(n.route.id, `${e} can only be used on routes that contain a unique "id"`),
    n.route.id
  );
}
function Rh() {
  return io("useRouteId");
}
function Lh() {
  var r;
  let e = v.useContext(lo),
    t = Ph("useRouteError"),
    n = io("useRouteError");
  return e !== void 0 ? e : (r = t.errors) == null ? void 0 : r[n];
}
function Nh() {
  let { router: e } = Ch("useNavigate"),
    t = io("useNavigate"),
    n = v.useRef(!1);
  return (
    Gc(() => {
      n.current = !0;
    }),
    v.useCallback(
      async (l, u = {}) => {
        Fe(n.current, Xc),
          n.current &&
            (typeof l == "number"
              ? e.navigate(l)
              : await e.navigate(l, { fromRouteId: t, ...u }));
      },
      [e, t]
    )
  );
}
var _a = {};
function qc(e, t, n) {
  !t && !_a[e] && ((_a[e] = !0), Fe(!1, n));
}
v.memo(Th);
function Th({ routes: e, future: t, state: n }) {
  return Jc(e, void 0, n, t);
}
function bc(e) {
  H(
    !1,
    "A <Route> is only ever to be used as the child of <Routes> element, never rendered directly. Please wrap your <Route> in a <Routes>."
  );
}
function zh({
  basename: e = "/",
  children: t = null,
  location: n,
  navigationType: r = "POP",
  navigator: l,
  static: u = !1,
}) {
  H(
    !fr(),
    "You cannot render a <Router> inside another <Router>. You should never have more than one in your app."
  );
  let i = e.replace(/^\/*/, "/"),
    o = v.useMemo(
      () => ({ basename: i, navigator: l, static: u, future: {} }),
      [i, l, u]
    );
  typeof n == "string" && (n = mn(n));
  let {
      pathname: a = "/",
      search: s = "",
      hash: h = "",
      state: m = null,
      key: p = "default",
    } = n,
    y = v.useMemo(() => {
      let S = gt(a, i);
      return S == null
        ? null
        : {
            location: { pathname: S, search: s, hash: h, state: m, key: p },
            navigationType: r,
          };
    }, [i, a, s, h, m, p, r]);
  return (
    Fe(
      y != null,
      `<Router basename="${i}"> is not able to match the URL "${a}${s}${h}" because it does not start with the basename, so the <Router> won't render anything.`
    ),
    y == null
      ? null
      : v.createElement(
          Be.Provider,
          { value: o },
          v.createElement(cr.Provider, { children: t, value: y })
        )
  );
}
function Oh({ children: e, location: t }) {
  return gh(oi(e), t);
}
function oi(e, t = []) {
  let n = [];
  return (
    v.Children.forEach(e, (r, l) => {
      if (!v.isValidElement(r)) return;
      let u = [...t, l];
      if (r.type === v.Fragment) {
        n.push.apply(n, oi(r.props.children, u));
        return;
      }
      H(
        r.type === bc,
        `[${
          typeof r.type == "string" ? r.type : r.type.name
        }] is not a <Route> component. All component children of <Routes> must be a <Route> or <React.Fragment>`
      ),
        H(
          !r.props.index || !r.props.children,
          "An index route cannot have child routes."
        );
      let i = {
        id: r.props.id || u.join("-"),
        caseSensitive: r.props.caseSensitive,
        element: r.props.element,
        Component: r.props.Component,
        index: r.props.index,
        path: r.props.path,
        loader: r.props.loader,
        action: r.props.action,
        hydrateFallbackElement: r.props.hydrateFallbackElement,
        HydrateFallback: r.props.HydrateFallback,
        errorElement: r.props.errorElement,
        ErrorBoundary: r.props.ErrorBoundary,
        hasErrorBoundary:
          r.props.hasErrorBoundary === !0 ||
          r.props.ErrorBoundary != null ||
          r.props.errorElement != null,
        shouldRevalidate: r.props.shouldRevalidate,
        handle: r.props.handle,
        lazy: r.props.lazy,
      };
      r.props.children && (i.children = oi(r.props.children, u)), n.push(i);
    }),
    n
  );
}
var Qr = "get",
  Kr = "application/x-www-form-urlencoded";
function Fl(e) {
  return e != null && typeof e.tagName == "string";
}
function Ih(e) {
  return Fl(e) && e.tagName.toLowerCase() === "button";
}
function Dh(e) {
  return Fl(e) && e.tagName.toLowerCase() === "form";
}
function Fh(e) {
  return Fl(e) && e.tagName.toLowerCase() === "input";
}
function Mh(e) {
  return !!(e.metaKey || e.altKey || e.ctrlKey || e.shiftKey);
}
function $h(e, t) {
  return e.button === 0 && (!t || t === "_self") && !Mh(e);
}
function ai(e = "") {
  return new URLSearchParams(
    typeof e == "string" || Array.isArray(e) || e instanceof URLSearchParams
      ? e
      : Object.keys(e).reduce((t, n) => {
          let r = e[n];
          return t.concat(Array.isArray(r) ? r.map((l) => [n, l]) : [[n, r]]);
        }, [])
  );
}
function jh(e, t) {
  let n = ai(e);
  return (
    t &&
      t.forEach((r, l) => {
        n.has(l) ||
          t.getAll(l).forEach((u) => {
            n.append(l, u);
          });
      }),
    n
  );
}
var Or = null;
function Uh() {
  if (Or === null)
    try {
      new FormData(document.createElement("form"), 0), (Or = !1);
    } catch {
      Or = !0;
    }
  return Or;
}
var Ah = new Set([
  "application/x-www-form-urlencoded",
  "multipart/form-data",
  "text/plain",
]);
function du(e) {
  return e != null && !Ah.has(e)
    ? (Fe(
        !1,
        `"${e}" is not a valid \`encType\` for \`<Form>\`/\`<fetcher.Form>\` and will default to "${Kr}"`
      ),
      null)
    : e;
}
function Bh(e, t) {
  let n, r, l, u, i;
  if (Dh(e)) {
    let o = e.getAttribute("action");
    (r = o ? gt(o, t) : null),
      (n = e.getAttribute("method") || Qr),
      (l = du(e.getAttribute("enctype")) || Kr),
      (u = new FormData(e));
  } else if (Ih(e) || (Fh(e) && (e.type === "submit" || e.type === "image"))) {
    let o = e.form;
    if (o == null)
      throw new Error(
        'Cannot submit a <button> or <input type="submit"> without a <form>'
      );
    let a = e.getAttribute("formaction") || o.getAttribute("action");
    if (
      ((r = a ? gt(a, t) : null),
      (n = e.getAttribute("formmethod") || o.getAttribute("method") || Qr),
      (l =
        du(e.getAttribute("formenctype")) ||
        du(o.getAttribute("enctype")) ||
        Kr),
      (u = new FormData(o, e)),
      !Uh())
    ) {
      let { name: s, type: h, value: m } = e;
      if (h === "image") {
        let p = s ? `${s}.` : "";
        u.append(`${p}x`, "0"), u.append(`${p}y`, "0");
      } else s && u.append(s, m);
    }
  } else {
    if (Fl(e))
      throw new Error(
        'Cannot submit element that is not <form>, <button>, or <input type="submit|image">'
      );
    (n = Qr), (r = null), (l = Kr), (i = e);
  }
  return (
    u && l === "text/plain" && ((i = u), (u = void 0)),
    { action: r, method: n.toLowerCase(), encType: l, formData: u, body: i }
  );
}
function oo(e, t) {
  if (e === !1 || e === null || typeof e > "u") throw new Error(t);
}
async function Hh(e, t) {
  if (e.id in t) return t[e.id];
  try {
    let n = await import(e.module);
    return (t[e.id] = n), n;
  } catch (n) {
    return (
      console.error(
        `Error loading route module \`${e.module}\`, reloading page...`
      ),
      console.error(n),
      window.__reactRouterContext && window.__reactRouterContext.isSpaMode,
      window.location.reload(),
      new Promise(() => {})
    );
  }
}
function Vh(e) {
  return e == null
    ? !1
    : e.href == null
    ? e.rel === "preload" &&
      typeof e.imageSrcSet == "string" &&
      typeof e.imageSizes == "string"
    : typeof e.rel == "string" && typeof e.href == "string";
}
async function Wh(e, t, n) {
  let r = await Promise.all(
    e.map(async (l) => {
      let u = t.routes[l.route.id];
      if (u) {
        let i = await Hh(u, n);
        return i.links ? i.links() : [];
      }
      return [];
    })
  );
  return Xh(
    r
      .flat(1)
      .filter(Vh)
      .filter((l) => l.rel === "stylesheet" || l.rel === "preload")
      .map((l) =>
        l.rel === "stylesheet"
          ? { ...l, rel: "prefetch", as: "style" }
          : { ...l, rel: "prefetch" }
      )
  );
}
function Ra(e, t, n, r, l, u) {
  let i = (a, s) => (n[s] ? a.route.id !== n[s].route.id : !0),
    o = (a, s) => {
      var h;
      return (
        n[s].pathname !== a.pathname ||
        (((h = n[s].route.path) == null ? void 0 : h.endsWith("*")) &&
          n[s].params["*"] !== a.params["*"])
      );
    };
  return u === "assets"
    ? t.filter((a, s) => i(a, s) || o(a, s))
    : u === "data"
    ? t.filter((a, s) => {
        var m;
        let h = r.routes[a.route.id];
        if (!h || !h.hasLoader) return !1;
        if (i(a, s) || o(a, s)) return !0;
        if (a.route.shouldRevalidate) {
          let p = a.route.shouldRevalidate({
            currentUrl: new URL(l.pathname + l.search + l.hash, window.origin),
            currentParams: ((m = n[0]) == null ? void 0 : m.params) || {},
            nextUrl: new URL(e, window.origin),
            nextParams: a.params,
            defaultShouldRevalidate: !0,
          });
          if (typeof p == "boolean") return p;
        }
        return !0;
      })
    : [];
}
function Qh(e, t) {
  return Kh(
    e
      .map((n) => {
        let r = t.routes[n.route.id];
        if (!r) return [];
        let l = [r.module];
        return r.imports && (l = l.concat(r.imports)), l;
      })
      .flat(1)
  );
}
function Kh(e) {
  return [...new Set(e)];
}
function Yh(e) {
  let t = {},
    n = Object.keys(e).sort();
  for (let r of n) t[r] = e[r];
  return t;
}
function Xh(e, t) {
  let n = new Set();
  return (
    new Set(t),
    e.reduce((r, l) => {
      let u = JSON.stringify(Yh(l));
      return n.has(u) || (n.add(u), r.push({ key: u, link: l })), r;
    }, [])
  );
}
function Gh(e) {
  let t =
    typeof e == "string"
      ? new URL(
          e,
          typeof window > "u" ? "server://singlefetch/" : window.location.origin
        )
      : e;
  return (
    t.pathname === "/"
      ? (t.pathname = "_root.data")
      : (t.pathname = `${t.pathname.replace(/\/$/, "")}.data`),
    t
  );
}
function Zh() {
  let e = v.useContext(yn);
  return (
    oo(
      e,
      "You must render this element inside a <DataRouterContext.Provider> element"
    ),
    e
  );
}
function Jh() {
  let e = v.useContext(Dl);
  return (
    oo(
      e,
      "You must render this element inside a <DataRouterStateContext.Provider> element"
    ),
    e
  );
}
var ao = v.createContext(void 0);
ao.displayName = "FrameworkContext";
function ef() {
  let e = v.useContext(ao);
  return (
    oo(e, "You must render this element inside a <HydratedRouter> element"), e
  );
}
function qh(e, t) {
  let n = v.useContext(ao),
    [r, l] = v.useState(!1),
    [u, i] = v.useState(!1),
    {
      onFocus: o,
      onBlur: a,
      onMouseEnter: s,
      onMouseLeave: h,
      onTouchStart: m,
    } = t,
    p = v.useRef(null);
  v.useEffect(() => {
    if ((e === "render" && i(!0), e === "viewport")) {
      let g = (f) => {
          f.forEach((c) => {
            i(c.isIntersecting);
          });
        },
        x = new IntersectionObserver(g, { threshold: 0.5 });
      return (
        p.current && x.observe(p.current),
        () => {
          x.disconnect();
        }
      );
    }
  }, [e]),
    v.useEffect(() => {
      if (r) {
        let g = setTimeout(() => {
          i(!0);
        }, 100);
        return () => {
          clearTimeout(g);
        };
      }
    }, [r]);
  let y = () => {
      l(!0);
    },
    S = () => {
      l(!1), i(!1);
    };
  return n
    ? e !== "intent"
      ? [u, p, {}]
      : [
          u,
          p,
          {
            onFocus: Rn(o, y),
            onBlur: Rn(a, S),
            onMouseEnter: Rn(s, y),
            onMouseLeave: Rn(h, S),
            onTouchStart: Rn(m, y),
          },
        ]
    : [!1, p, {}];
}
function Rn(e, t) {
  return (n) => {
    e && e(n), n.defaultPrevented || t(n);
  };
}
function bh({ page: e, ...t }) {
  let { router: n } = Zh(),
    r = v.useMemo(() => Bc(n.routes, e, n.basename), [n.routes, e, n.basename]);
  return r ? v.createElement(tm, { page: e, matches: r, ...t }) : null;
}
function em(e) {
  let { manifest: t, routeModules: n } = ef(),
    [r, l] = v.useState([]);
  return (
    v.useEffect(() => {
      let u = !1;
      return (
        Wh(e, t, n).then((i) => {
          u || l(i);
        }),
        () => {
          u = !0;
        }
      );
    }, [e, t, n]),
    r
  );
}
function tm({ page: e, matches: t, ...n }) {
  let r = Et(),
    { manifest: l, routeModules: u } = ef(),
    { loaderData: i, matches: o } = Jh(),
    a = v.useMemo(() => Ra(e, t, o, l, r, "data"), [e, t, o, l, r]),
    s = v.useMemo(() => Ra(e, t, o, l, r, "assets"), [e, t, o, l, r]),
    h = v.useMemo(() => {
      if (e === r.pathname + r.search + r.hash) return [];
      let y = new Set(),
        S = !1;
      if (
        (t.forEach((x) => {
          var c;
          let f = l.routes[x.route.id];
          !f ||
            !f.hasLoader ||
            ((!a.some((d) => d.route.id === x.route.id) &&
              x.route.id in i &&
              (c = u[x.route.id]) != null &&
              c.shouldRevalidate) ||
            f.hasClientLoader
              ? (S = !0)
              : y.add(x.route.id));
        }),
        y.size === 0)
      )
        return [];
      let g = Gh(e);
      return (
        S &&
          y.size > 0 &&
          g.searchParams.set(
            "_routes",
            t
              .filter((x) => y.has(x.route.id))
              .map((x) => x.route.id)
              .join(",")
          ),
        [g.pathname + g.search]
      );
    }, [i, r, l, a, t, e, u]),
    m = v.useMemo(() => Qh(s, l), [s, l]),
    p = em(s);
  return v.createElement(
    v.Fragment,
    null,
    h.map((y) =>
      v.createElement("link", {
        key: y,
        rel: "prefetch",
        as: "fetch",
        href: y,
        ...n,
      })
    ),
    m.map((y) =>
      v.createElement("link", { key: y, rel: "modulepreload", href: y, ...n })
    ),
    p.map(({ key: y, link: S }) => v.createElement("link", { key: y, ...S }))
  );
}
function nm(...e) {
  return (t) => {
    e.forEach((n) => {
      typeof n == "function" ? n(t) : n != null && (n.current = t);
    });
  };
}
var tf =
  typeof window < "u" &&
  typeof window.document < "u" &&
  typeof window.document.createElement < "u";
try {
  tf && (window.__reactRouterVersion = "7.1.3");
} catch {}
function rm({ basename: e, children: t, window: n }) {
  let r = v.useRef();
  r.current == null && (r.current = Wp({ window: n, v5Compat: !0 }));
  let l = r.current,
    [u, i] = v.useState({ action: l.action, location: l.location }),
    o = v.useCallback(
      (a) => {
        v.startTransition(() => i(a));
      },
      [i]
    );
  return (
    v.useLayoutEffect(() => l.listen(o), [l, o]),
    v.createElement(zh, {
      basename: e,
      children: t,
      location: u.location,
      navigationType: u.action,
      navigator: l,
    })
  );
}
var nf = /^(?:[a-z][a-z0-9+.-]*:|\/\/)/i,
  rf = v.forwardRef(function (
    {
      onClick: t,
      discover: n = "render",
      prefetch: r = "none",
      relative: l,
      reloadDocument: u,
      replace: i,
      state: o,
      target: a,
      to: s,
      preventScrollReset: h,
      viewTransition: m,
      ...p
    },
    y
  ) {
    let { basename: S } = v.useContext(Be),
      g = typeof s == "string" && nf.test(s),
      x,
      f = !1;
    if (typeof s == "string" && g && ((x = s), tf))
      try {
        let I = new URL(window.location.href),
          N = s.startsWith("//") ? new URL(I.protocol + s) : new URL(s),
          ce = gt(N.pathname, S);
        N.origin === I.origin && ce != null
          ? (s = ce + N.search + N.hash)
          : (f = !0);
      } catch {
        Fe(
          !1,
          `<Link to="${s}"> contains an invalid URL which will probably break when clicked - please update to a valid URL path.`
        );
      }
    let c = yh(s, { relative: l }),
      [d, w, E] = qh(r, p),
      R = om(s, {
        replace: i,
        state: o,
        target: a,
        preventScrollReset: h,
        relative: l,
        viewTransition: m,
      });
    function _(I) {
      t && t(I), I.defaultPrevented || R(I);
    }
    let L = v.createElement("a", {
      ...p,
      ...E,
      href: x || c,
      onClick: f || u ? t : _,
      ref: nm(y, w),
      target: a,
      "data-discover": !g && n === "render" ? "true" : void 0,
    });
    return d && !g
      ? v.createElement(v.Fragment, null, L, v.createElement(bh, { page: c }))
      : L;
  });
rf.displayName = "Link";
var lm = v.forwardRef(function (
  {
    "aria-current": t = "page",
    caseSensitive: n = !1,
    className: r = "",
    end: l = !1,
    style: u,
    to: i,
    viewTransition: o,
    children: a,
    ...s
  },
  h
) {
  let m = dr(i, { relative: s.relative }),
    p = Et(),
    y = v.useContext(Dl),
    { navigator: S, basename: g } = v.useContext(Be),
    x = y != null && pm(m) && o === !0,
    f = S.encodeLocation ? S.encodeLocation(m).pathname : m.pathname,
    c = p.pathname,
    d =
      y && y.navigation && y.navigation.location
        ? y.navigation.location.pathname
        : null;
  n ||
    ((c = c.toLowerCase()),
    (d = d ? d.toLowerCase() : null),
    (f = f.toLowerCase())),
    d && g && (d = gt(d, g) || d);
  const w = f !== "/" && f.endsWith("/") ? f.length - 1 : f.length;
  let E = c === f || (!l && c.startsWith(f) && c.charAt(w) === "/"),
    R =
      d != null &&
      (d === f || (!l && d.startsWith(f) && d.charAt(f.length) === "/")),
    _ = { isActive: E, isPending: R, isTransitioning: x },
    L = E ? t : void 0,
    I;
  typeof r == "function"
    ? (I = r(_))
    : (I = [
        r,
        E ? "active" : null,
        R ? "pending" : null,
        x ? "transitioning" : null,
      ]
        .filter(Boolean)
        .join(" "));
  let N = typeof u == "function" ? u(_) : u;
  return v.createElement(
    rf,
    {
      ...s,
      "aria-current": L,
      className: I,
      ref: h,
      style: N,
      to: i,
      viewTransition: o,
    },
    typeof a == "function" ? a(_) : a
  );
});
lm.displayName = "NavLink";
var um = v.forwardRef(
  (
    {
      discover: e = "render",
      fetcherKey: t,
      navigate: n,
      reloadDocument: r,
      replace: l,
      state: u,
      method: i = Qr,
      action: o,
      onSubmit: a,
      relative: s,
      preventScrollReset: h,
      viewTransition: m,
      ...p
    },
    y
  ) => {
    let S = fm(),
      g = dm(o, { relative: s }),
      x = i.toLowerCase() === "get" ? "get" : "post",
      f = typeof o == "string" && nf.test(o),
      c = (d) => {
        if ((a && a(d), d.defaultPrevented)) return;
        d.preventDefault();
        let w = d.nativeEvent.submitter,
          E = (w == null ? void 0 : w.getAttribute("formmethod")) || i;
        S(w || d.currentTarget, {
          fetcherKey: t,
          method: E,
          navigate: n,
          replace: l,
          state: u,
          relative: s,
          preventScrollReset: h,
          viewTransition: m,
        });
      };
    return v.createElement("form", {
      ref: y,
      method: x,
      action: g,
      onSubmit: r ? a : c,
      ...p,
      "data-discover": !f && e === "render" ? "true" : void 0,
    });
  }
);
um.displayName = "Form";
function im(e) {
  return `${e} must be used within a data router.  See https://reactrouter.com/en/main/routers/picking-a-router.`;
}
function lf(e) {
  let t = v.useContext(yn);
  return H(t, im(e)), t;
}
function om(
  e,
  {
    target: t,
    replace: n,
    state: r,
    preventScrollReset: l,
    relative: u,
    viewTransition: i,
  } = {}
) {
  let o = Zc(),
    a = Et(),
    s = dr(e, { relative: u });
  return v.useCallback(
    (h) => {
      if ($h(h, t)) {
        h.preventDefault();
        let m = n !== void 0 ? n : lr(a) === lr(s);
        o(e, {
          replace: m,
          state: r,
          preventScrollReset: l,
          relative: u,
          viewTransition: i,
        });
      }
    },
    [a, o, s, n, r, t, e, l, u, i]
  );
}
function am(e) {
  Fe(
    typeof URLSearchParams < "u",
    "You cannot use the `useSearchParams` hook in a browser that does not support the URLSearchParams API. If you need to support Internet Explorer 11, we recommend you load a polyfill such as https://github.com/ungap/url-search-params."
  );
  let t = v.useRef(ai(e)),
    n = v.useRef(!1),
    r = Et(),
    l = v.useMemo(() => jh(r.search, n.current ? null : t.current), [r.search]),
    u = Zc(),
    i = v.useCallback(
      (o, a) => {
        const s = ai(typeof o == "function" ? o(l) : o);
        (n.current = !0), u("?" + s, a);
      },
      [u, l]
    );
  return [l, i];
}
var sm = 0,
  cm = () => `__${String(++sm)}__`;
function fm() {
  let { router: e } = lf("useSubmit"),
    { basename: t } = v.useContext(Be),
    n = Rh();
  return v.useCallback(
    async (r, l = {}) => {
      let { action: u, method: i, encType: o, formData: a, body: s } = Bh(r, t);
      if (l.navigate === !1) {
        let h = l.fetcherKey || cm();
        await e.fetch(h, n, l.action || u, {
          preventScrollReset: l.preventScrollReset,
          formData: a,
          body: s,
          formMethod: l.method || i,
          formEncType: l.encType || o,
          flushSync: l.flushSync,
        });
      } else
        await e.navigate(l.action || u, {
          preventScrollReset: l.preventScrollReset,
          formData: a,
          body: s,
          formMethod: l.method || i,
          formEncType: l.encType || o,
          replace: l.replace,
          state: l.state,
          fromRouteId: n,
          flushSync: l.flushSync,
          viewTransition: l.viewTransition,
        });
    },
    [e, t, n]
  );
}
function dm(e, { relative: t } = {}) {
  let { basename: n } = v.useContext(Be),
    r = v.useContext(et);
  H(r, "useFormAction must be used inside a RouteContext");
  let [l] = r.matches.slice(-1),
    u = { ...dr(e || ".", { relative: t }) },
    i = Et();
  if (e == null) {
    u.search = i.search;
    let o = new URLSearchParams(u.search),
      a = o.getAll("index");
    if (a.some((h) => h === "")) {
      o.delete("index"),
        a.filter((m) => m).forEach((m) => o.append("index", m));
      let h = o.toString();
      u.search = h ? `?${h}` : "";
    }
  }
  return (
    (!e || e === ".") &&
      l.route.index &&
      (u.search = u.search ? u.search.replace(/^\?/, "?index&") : "?index"),
    n !== "/" && (u.pathname = u.pathname === "/" ? n : Xe([n, u.pathname])),
    lr(u)
  );
}
function pm(e, t = {}) {
  let n = v.useContext(Yc);
  H(
    n != null,
    "`useViewTransitionState` must be used within `react-router-dom`'s `RouterProvider`.  Did you accidentally import `RouterProvider` from `react-router`?"
  );
  let { basename: r } = lf("useViewTransitionState"),
    l = dr(e, { relative: t.relative });
  if (!n.isTransitioning) return !1;
  let u = gt(n.currentLocation.pathname, r) || n.currentLocation.pathname,
    i = gt(n.nextLocation.pathname, r) || n.nextLocation.pathname;
  return vl(l.pathname, i) != null || vl(l.pathname, u) != null;
}
new TextEncoder();
const La = (e) => {
    let t;
    const n = new Set(),
      r = (s, h) => {
        const m = typeof s == "function" ? s(t) : s;
        if (!Object.is(m, t)) {
          const p = t;
          (t =
            h ?? (typeof m != "object" || m === null)
              ? m
              : Object.assign({}, t, m)),
            n.forEach((y) => y(t, p));
        }
      },
      l = () => t,
      o = {
        setState: r,
        getState: l,
        getInitialState: () => a,
        subscribe: (s) => (n.add(s), () => n.delete(s)),
      },
      a = (t = e(r, l, o));
    return o;
  },
  hm = (e) => (e ? La(e) : La),
  mm = (e) => e;
function ym(e, t = mm) {
  const n = ho.useSyncExternalStore(
    e.subscribe,
    () => t(e.getState()),
    () => t(e.getInitialState())
  );
  return ho.useDebugValue(n), n;
}
const vm = (e) => {
    const t = hm(e),
      n = (r) => ym(t, r);
    return Object.assign(n, t), n;
  },
  gm = (e) => vm(e),
  uf = gm((e) => ({
    sharedLinks: [],
    addSharedLink: (t) => e((n) => ({ sharedLinks: [...n.sharedLinks, t] })),
  }));
function wm() {
  const e = uf((t) => t.sharedLinks);
  return We.jsxs("div", {
    children: [
      We.jsx("h1", { children: "공유된 링크" }),
      We.jsx("ul", {
        children: e.map((t, n) => We.jsx("li", { children: t }, n)),
      }),
    ],
  });
}
function Sm() {
  const { addSharedLink: e } = uf(),
    [t] = am();
  return (
    v.useEffect(() => {
      const n = t.get("url");
      n && e(n);
    }, [t, e]),
    We.jsx(wm, {})
  );
}
pu.createRoot(document.getElementById("root")).render(
  We.jsx(rm, {
    children: We.jsx(Oh, {
      children: We.jsx(bc, { path: "/*", element: We.jsx(Sm, {}) }),
    }),
  })
);
