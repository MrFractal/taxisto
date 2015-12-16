
/**
 * Created by max on 09.02.14.
 */

@interface BusyDriverRequest : DriverRequest {
  BOOL value;
}

@property(nonatomic) BOOL value;
- (void) init;
- (void) setValue:(BOOL)value;
@end
