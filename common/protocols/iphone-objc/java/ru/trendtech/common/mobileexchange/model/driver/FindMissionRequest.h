
/**
 * Created by max on 06.02.14.
 */

@interface FindMissionRequest : DriverRequest {
  int radius;
}

@property(nonatomic) int radius;
- (void) setRadius:(int)radius;
@end
