
/**
 * Created by max on 13.02.14.
 */

@interface MissionFinishedRequest : DriverRequest {
  long missionId;
}

@property(nonatomic) long missionId;
- (void) setMissionId:(long)missionId;
@end
