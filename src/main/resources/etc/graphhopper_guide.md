# GraphHopper 라이브러리 시작하기

GraphHopper는 오픈 소스 경로 탐색 엔진으로, Java 라이브러리 또는 독립형 웹 서버로 사용할 수 있습니다. OpenStreetMap(OSM) 데이터를 사용하여 경로 계산, 이동 시간 예측, 경로 최적화 등 다양한 기능을 제공합니다.

## 주요 특징

- **경로 탐색 (Routing)**: 자동차, 자전거, 도보 등 다양한 프로필에 대한 최단 또는 최적 경로를 계산합니다.
- **턴 바이 턴 길 안내 (Turn-by-turn Navigation)**: 경로에 대한 상세한 방향 정보를 제공합니다.
- **등시선 (Isochrone)**: 특정 지점에서 주어진 시간 내에 도달할 수 있는 모든 지역을 계산합니다. (예: "내 위치에서 15분 안에 갈 수 있는 모든 곳")
- **경로 최적화 (Route Optimization)**: 여러 경유지를 가장 효율적인 순서로 방문하는 경로(TSP, VRP)를 찾습니다.
- **맵 매칭 (Map Matching)**: GPS 좌표 시퀀스를 실제 도로 네트워크에 맞는 경로로 변환합니다.
- **고도 데이터**: 경로의 고도 프로필을 계산에 포함할 수 있습니다.

## 시작하기 (Java 라이브러리)

### 1. 준비물

- **Java Development Kit (JDK)**: 최신 버전의 JDK (보통 8 이상)가 필요합니다.
- **Maven 또는 Gradle**: 의존성 관리를 위한 빌드 도구입니다.
- **OpenStreetMap(OSM) 데이터**: 경로를 계산할 지역의 `.osm.pbf` 형식 파일이 필요합니다. [Geofabrik](http://download.geofabrik.de/) 등에서 다운로드할 수 있습니다.

### 2. 의존성 추가

`pom.xml` (Maven) 또는 `build.gradle` (Gradle) 파일에 GraphHopper 의존성을 추가합니다.

**Maven (`pom.xml`):**
```xml
<dependency>
    <groupId>com.graphhopper</groupId>
    <artifactId>graphhopper-core</artifactId>
    <version>8.0</version> <!-- 최신 버전 확인 후 사용 -->
</dependency>
```

### 3. 기본 사용법 (Java 코드 예시)

다음은 지정된 OSM 데이터로 GraphHopper 인스턴스를 설정하고, 두 지점 간의 경로를 찾는 간단한 예시입니다.

```java
import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.util.PointList;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.ResponsePath;

public class GraphHopperExample {

    public static void main(String[] args) {
        // 1. GraphHopper 인스턴스 생성 및 설정
        GraphHopper hopper = new GraphHopper();
        // OSM PBF 파일 경로
        hopper.setOSMFile("path/to/your-area.osm.pbf");
        // 경로 계산 결과를 저장할 그래프 폴더 위치
        hopper.setGraphHopperLocation("./graphhopper_graph_folder");
        // 경로 계산 프로필 설정 (여기서는 자동차)
        hopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest").setTurnCosts(false));

        // 2. 그래프 생성 (최초 실행 시 시간이 소요됨)
        hopper.importOrLoad();

        // 3. 경로 요청 생성 (출발지: 위도,경도 -> 도착지: 위도,경도)
        GHRequest request = new GHRequest(49.416, 8.682, 49.421, 8.685).setProfile("car");

        // 4. 경로 계산
        GHResponse response = hopper.get(request);

        // 5. 결과 처리
        if (response.hasErrors()) {
            // 오류 처리
            System.out.println("경로 계산 중 오류 발생: " + response.getErrors());
            return;
        }

        // 가장 좋은 경로 하나를 가져옴
        ResponsePath path = response.getBest();

        // 경로 정보 출력
        double distance = path.getDistance(); // 미터 단위 거리
        long timeInMs = path.getTime();       // 밀리초 단위 시간
        PointList points = path.getPoints();  // 경로를 구성하는 좌표 목록

        System.out.printf("경로 거리: %.2f km\n", distance / 1000);
        System.out.printf("예상 소요 시간: %.1f 분\n", timeInMs / 60000.0);
        System.out.println("경로 좌표 수: " + points.size());

        // 6. 리소스 정리
        hopper.close();
    }
}
```

## 더 알아보기

- **[공식 문서 (Quickstart)](https://github.com/graphhopper/graphhopper/blob/master/docs/core/quickstart.md)**: 개발자를 위한 가장 좋은 시작점입니다.
- **[GraphHopper GitHub 저장소](https://github.com/graphhopper/graphhopper)**: 전체 소스 코드, 이슈 트래커 및 추가 문서를 확인할 수 있습니다.
- **[GraphHopper Directions API](https://www.graphhopper.com/products/)**: 상업용으로 제공되는 강력한 API 서비스입니다. 라우팅 외에 행렬 계산, 경로 최적화 등 고급 기능을 제공합니다.
