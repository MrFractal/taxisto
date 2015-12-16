
/**
 * Created by max on 13.02.14.
 */

@interface CancelMissionResponse : NSObject {
  long missionId;
  BOOL completed;
}

@property(nonatomic) long missionId;
@property(nonatomic) BOOL completed;
- (void) setMissionId:(long)missionId;
- (void) setCompleted:(BOOL)completed;
@end
