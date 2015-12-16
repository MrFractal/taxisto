
/**
 * Created by max on 13.02.14.
 */

@interface MissionInfoRequest : NSObject {
  long missionId;
}

@property(nonatomic) long missionId;
- (void) setMissionId:(long)missionId;
@end
