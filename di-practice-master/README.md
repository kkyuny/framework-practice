## DI 프레임워크 만들기 실습
- 생성자 호출 시 필요한 의존성을 BeanFactory가 리플렉션을 통해 주입하도록 코드를 구현했다.
## 실습코드
### BeanFactory
1. 필드 선언부
``` java
private Set<Class<?>> preInstantiatedBeans;
private Map<Class<?>, Object> beans = new HashMap<>();
```
- `preInstantiatedBeans`: 미리 스캔해서 인스턴스화할 클래스 목록(set으로 선언)
- `beans`: 클래스 타입과 해당 인스턴스를 매핑하는 저장소(스프링 bean 컨테이너 역할)

2. 생성자
``` java
public BeanFactory(Set<Class<?>> preInstantiatedBeans) {
    this.preInstantiatedBeans = preInstantiatedBeans;
    initialize();
}
```
- 외부에서 주입받은 클래스 set을 `preInstantiatedBeans`에 저장
- `initialize()` 호출로 실제 인스턴스 생성 및 등록 시작

3. initialize()
``` java
@SuppressWarnings("unchecked")
public void initialize() {
    for (Class<?> clazz : preInstantiatedBeans) {
        Object instance = createInstance(clazz);
        beans.put(clazz, instance);
    }
}
```
- `preInstantiatedBeans`으로 각 클래스 타입을 순회하며 인스턴스를 생성하여 `beans`에 저장

4. createInstance()
``` java
private Object createInstance(Class<?> concreteClass) {
    Constructor<?> constructor = findConstructor(concreteClass);
    List<Object> parameters = new ArrayList<>();

    for (Class<?> typeClass : Objects.requireNonNull(constructor).getParameterTypes()) {
        parameters.add(getBean(typeClass));
    }

    try {
        return constructor.newInstance(parameters.toArray());
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
    }
}
```
- **UserController**
``` java
private UserService userService;
@Inject
public UserController(UserService userService) {
    this.userService = userService;
}
```
- 주어진 클래스의 생성자를 `findConstructor()`로 찾음
- 생성자의 매개변수 타임을 순회하며 해당타입의 Bean을 `getBean()`으로 가져와 parameters에 추가
- `return constructor.newInstance(parameters.toArray())`를 이용해 해당 객체의 인스턴스 생성 및 값을 리턴
- `constructor.newInstance()`: 리플렉션을 이용해 런타임중에 생성자를 실행해서 객체를 생성하는 방법이다.
- 따라서 createInstance()을 통한 인스턴스 생성 시
- return constructor.newInstance(parameters.toArray())가
- return new UserController((UserService) parameters[0])를 실행한 것과 동일한 효과를 낸다.

5. findConstructor()
``` java
private Constructor<?> findConstructor(Class<?> concreteClass) {
    Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);

    if (Objects.nonNull(constructor)) {
        return constructor;
    }

    return concreteClass.getConstructors()[0];
}
```
- `BeanFactoryUtils.getInjectedConstructor()`로 주입용 생성자(ex:@Inject)를 검색
- 없다면 기본적으로 첫 번째 public 생성자 반환

6. getBean()
``` java
public <T> T getBean(Class<T> requiredType) {
    return (T) beans.get(requiredType);
}
```
- 타입을 키로 하여 해당 타입의 Bean 인스턴스를 제네릭으로 형 변환 후 리턴

### BeanFactoryUtils
1. getInjectedConstructor()
``` java
@SuppressWarnings({"rawtypes", "unchecked"})
public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
    Set<Constructor> injectedConstructors = getAllConstructors(
        clazz, ReflectionUtilsPredicates.withAnnotation(Inject.class)
    );
    if (injectedConstructors.isEmpty()) {
        return null;
    }
    return injectedConstructors.iterator().next();
}
```
- 역할: 전달받은 클래스(clazz)에서 @Inject 애노테이션이 붙은 생성자를 찾아서 반환한다.
- `getAllConstructors(clazz, ReflectionUtilsPredicates.withAnnotation(Inject.class))`: 전달받은 클래스에서 @Inject 어노테이션이 붙은 생성자들을 반환
